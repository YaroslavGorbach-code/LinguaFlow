package com.example.yaroslavhorach.common.helpers

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

class AudioRecorder @Inject constructor() {

    private var audioRecord: AudioRecord? = null
    private val bufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private val _amplitudeFlow = MutableStateFlow(0)
    val amplitudeFlow = _amplitudeFlow.asStateFlow()

    private val _isRecordingFlow = MutableStateFlow(false)
    val isRecordingFlow = _isRecordingFlow.asStateFlow()

    private val _isSpeakingFlow = MutableStateFlow(false)
    val isSpeakingFlow = _isSpeakingFlow.asStateFlow()

    private val _secondsLeftFlow = MutableStateFlow(0)
    val secondsLeftFlow = _secondsLeftFlow.asStateFlow()

    private var recordingJob: Job? = null
    private var silenceTimerJob: Job? = null

    private val silenceDurationMillis = 6000L
    private val triggerSilenceDurationMillis = 5000L

    @SuppressLint("MissingPermission")
    fun start() {
        if (_isRecordingFlow.value) return
        _isRecordingFlow.value = true

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

                    val minAmplitude = 1200

                    if (max < minAmplitude) {
                        _isSpeakingFlow.value = false
                        if (silenceTimerJob == null) {
                            silenceTimerJob = launch {
                                var millisLeft = silenceDurationMillis
                                while (millisLeft > 0 && _isRecordingFlow.value && _amplitudeFlow.value < minAmplitude) {
                                    _secondsLeftFlow.value = if(triggerSilenceDurationMillis >= millisLeft) (millisLeft / 1000).toInt() else 0

                                    delay(1000)
                                    millisLeft -= 1000
                                }
                                if (_isRecordingFlow.value && _amplitudeFlow.value < minAmplitude) {
                                    stop()
                                }
                            }
                        }
                    } else {
                        _isSpeakingFlow.value = true

                        silenceTimerJob?.cancel()
                        silenceTimerJob = null

                        _secondsLeftFlow.value = 0
                    }
                }
                delay(16)
            }
        }
    }

    private fun stop() {
        if (!_isRecordingFlow.value) return
        _isRecordingFlow.value = false

        recordingJob?.cancel()
        recordingJob = null

        silenceTimerJob?.cancel()
        silenceTimerJob = null

        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null

        _secondsLeftFlow.value = 0
    }
}