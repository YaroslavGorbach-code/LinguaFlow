package com.example.yaroslavhorach.common.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import kotlin.math.absoluteValue

class AudioRecorder @Inject constructor() {
    companion object {
        private const val MAX_AUTO_RECORDING_STOP_ATTEMPTS = 2
    }

    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private var inputStream: FileInputStream? = null
    private var audioRecord: AudioRecord? = null
    private var outputStream: FileOutputStream? = null
    private var outputFile: File? = null
    private var attemptsToAutoReconnoitring = 0

    private var canIncreasAutoRecordingAttempts = true

    private val bufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private val _amplitudeFlow = MutableStateFlow(0)
    val amplitudeFlow = _amplitudeFlow.asStateFlow()

    private val _isRecordingFlow = MutableStateFlow(false)
    val isRecordingFlow = _isRecordingFlow.asStateFlow()

    private val _isPlayingPausedFlow = MutableStateFlow(false)
    val isPlayingPausedFlow = _isPlayingPausedFlow.asStateFlow()

    private val _isRationalToAllowStopManually = MutableStateFlow(true)
    val isRationalToAllowStopManually = _isRationalToAllowStopManually.asStateFlow()

    private val _onStopRecordingFlow = MutableSharedFlow<Unit>(1)
    val onStopRecordingFlow = _onStopRecordingFlow.asSharedFlow()

    private val _isSpeakingFlow = MutableStateFlow(false)
    val isSpeakingFlow = _isSpeakingFlow.asStateFlow()

    private val _playProgressFlow = MutableStateFlow(0f)
    val playProgressFlow: StateFlow<Float> = _playProgressFlow.asStateFlow()

    private val _secondsLeftFlow = MutableStateFlow(0)
    val secondsLeftFlow = _secondsLeftFlow.asStateFlow()

    private var recordingJob: Job? = null
    private var silenceTimerJob: Job? = null

    private val triggerSilenceDurationMillis = 10000L
    private val allowedSilenceDurationMillis = triggerSilenceDurationMillis + 2000

    @SuppressLint("MissingPermission")
    fun startRecording(context: Context) {
        if (_isRecordingFlow.value) return
        _isRecordingFlow.value = true

        outputFile?.delete()
        outputFile = File.createTempFile("recording_", ".pcm", context.cacheDir)
        outputStream = FileOutputStream(outputFile)
        _playProgressFlow.value = 0f

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            44100,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()

        recordingJob = CoroutineScope(Dispatchers.Default).launch {
            val buffer = ShortArray(bufferSize)

            while (_isRecordingFlow.value) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0

                if (read > 0) {
                    val max = buffer.maxOrNull()?.toInt()?.absoluteValue ?: 0
                    _amplitudeFlow.value = max

                    val byteBuffer = ByteBuffer.allocate(read * 2).order(ByteOrder.LITTLE_ENDIAN)

                    for (i in 0 until read) byteBuffer.putShort(buffer[i])
                    outputStream?.write(byteBuffer.array())

                    val minAmplitude = 1200
                    if (max < minAmplitude) {
                        _isSpeakingFlow.value = false

                        if (silenceTimerJob == null) {

                            silenceTimerJob = launch {
                                var millisLeft = allowedSilenceDurationMillis

                                while (millisLeft > 0 && _isRecordingFlow.value && _amplitudeFlow.value < minAmplitude) {
                                    _secondsLeftFlow.value = if (triggerSilenceDurationMillis >= millisLeft) {
                                        if (attemptsToAutoReconnoitring >= MAX_AUTO_RECORDING_STOP_ATTEMPTS && canIncreasAutoRecordingAttempts) {
                                            _isRationalToAllowStopManually.value = true
                                        }
                                        attemptsToAutoReconnoitring = attemptsToAutoReconnoitring.inc()
                                        canIncreasAutoRecordingAttempts = false

                                        (millisLeft / 1000).toInt()
                                    } else {
                                        0
                                    }

                                    delay(1000)
                                    millisLeft -= 1000
                                }

                                if (_isRecordingFlow.value && _amplitudeFlow.value < minAmplitude) {

                                    stopRecording()
                                }
                            }
                        }
                    } else {
                        _isSpeakingFlow.value = true
                        silenceTimerJob?.cancel()
                        silenceTimerJob = null
                        _secondsLeftFlow.value = 0
                        canIncreasAutoRecordingAttempts = true
                    }
                }
                delay(16)
            }
        }
    }

    fun playLastRecording() {
        if (_isPlayingPausedFlow.value ) {
            resumePlayback()
        }

        val file = outputFile ?: return
        if (playbackJob != null) return

        val playBufferSize = AudioTrack.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            44100,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            playBufferSize,
            AudioTrack.MODE_STREAM
        )

        inputStream = FileInputStream(file)
        val totalBytes = file.length()
        val buffer = ByteArray(playBufferSize)

        playbackJob = CoroutineScope(Dispatchers.IO).launch {
            var bytesRead = 0L
            var read: Int

            audioTrack?.play()

            try {
                while ((inputStream?.read(buffer).also { read = it ?: -1 } ?: -1) > 0) {
                    while (_isPlayingPausedFlow.value) {
                        delay(100) // wait if paused
                    }

                    audioTrack?.write(buffer, 0, read)
                    bytesRead += read
                    _playProgressFlow.value = bytesRead.toFloat() / totalBytes.toFloat()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stopPlayback()
            }
        }
    }

    fun pausePlayback() {
        if (audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING) {
            _isPlayingPausedFlow.value = true
            audioTrack?.pause()
        }
    }

    suspend fun stopRecording() {
        if (!_isRecordingFlow.value) return
        _isRecordingFlow.value = false

        recordingJob?.cancel()
        recordingJob = null

        silenceTimerJob?.cancel()
        silenceTimerJob = null

        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null

        outputStream?.close()
        outputStream = null

        _secondsLeftFlow.value = 0
        _onStopRecordingFlow.emit(Unit)
        attemptsToAutoReconnoitring = 0
//        _isRationalToAllowStopManually.value = false
    }

    private fun resumePlayback() {
        if (_isPlayingPausedFlow.value) {
            _isPlayingPausedFlow.value = false
            audioTrack?.play()
        }
    }

    private fun stopPlayback() {
        playbackJob?.cancel()
        playbackJob = null

        inputStream?.close()
        inputStream = null

        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null

        _playProgressFlow.value = 1f
        _isPlayingPausedFlow.value = false
    }
}