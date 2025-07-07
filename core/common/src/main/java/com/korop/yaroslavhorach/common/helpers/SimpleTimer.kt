package com.korop.yaroslavhorach.common.helpers

class SimpleTimer {
    private var startTime: Long = 0L
    private var endTime: Long = 0L
    private var isRunning: Boolean = false

    fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
        endTime = 0L
    }

    fun stop() {
        if (isRunning) {
            endTime = System.currentTimeMillis()
            isRunning = false
        }
    }

    fun reset() {
        startTime = 0L
        endTime = 0L
        isRunning = false
    }

    fun getElapsedTimeMillis(): Long {
        return when {
            isRunning -> System.currentTimeMillis() - startTime
            endTime > 0L -> endTime - startTime
            else -> 0L
        }
    }

    fun isRunning(): Boolean = isRunning
}