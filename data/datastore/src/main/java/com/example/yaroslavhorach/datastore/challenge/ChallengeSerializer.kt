package com.example.yaroslavhorach.datastore.challenge

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.example.yaroslavhorach.datastore.ChallengeProgress
import java.io.InputStream
import java.io.OutputStream

object ChallengeSerializer : Serializer<ChallengeProgress> {
    override val defaultValue: ChallengeProgress = ChallengeProgress.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ChallengeProgress {
        return try {
            ChallengeProgress.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ChallengeProgress, output: OutputStream) = t.writeTo(output)
}