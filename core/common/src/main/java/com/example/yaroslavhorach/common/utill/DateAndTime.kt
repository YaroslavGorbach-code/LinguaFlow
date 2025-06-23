package com.example.yaroslavhorach.common.utill

import android.annotation.SuppressLint
import android.content.Context
import com.example.yaroslavhorach.common.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.timeToToHoursMinutes(context: Context, format: TimeFormat): String {
    val minutes = (this / (1000 * 60) % 60)
    val hours = (this / (1000 * 60 * 60) % 24)

    when (format) {
        TimeFormat.SHORT -> {
            if (minutes > 0 && hours <= 0) {
                return context.resources.getString(R.string.minutes_short_format, minutes.toInt().toString())
            }

            if (hours > 0 && minutes <= 0) {
                return context.resources.getString(R.string.hours_short_format, hours.toInt().toString())
            }

            if (hours > 0 && minutes > 0) {
                return context.resources.getString(
                    R.string.hours_short_format,
                    hours.toInt().toString()
                ) + " " + context.resources.getString(
                    R.string.minutes_short_format,
                    minutes.toInt().toString()
                )
            }
        }
        TimeFormat.FULL -> {
            if (minutes > 0 && hours <= 0) {
                return context.resources.getQuantityString(
                    R.plurals.plurals_minutes,
                    minutes.toInt(),
                    minutes.toInt()
                )
            }

            if (hours > 0 && minutes <= 0) {
                return context.resources.getQuantityString(
                    R.plurals.plurals_hours,
                    hours.toInt(),
                    hours.toInt()
                )
            }

            if (hours > 0 && minutes > 0) {
                return context.resources.getQuantityString(
                    R.plurals.plurals_hours,
                    hours.toInt(),
                    hours.toInt()
                ) + " " + context.resources.getQuantityString(
                    R.plurals.plurals_minutes,
                    minutes.toInt(),
                    minutes.toInt()
                )
            }
        }
    }

    return ""
}

@SuppressLint("DefaultLocale")
fun Long.toMinutesSecondsFormat(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d", minutes, seconds)
}

fun Long.isToday(): Boolean {
    val calendar = Calendar.getInstance().apply { timeInMillis = this@isToday }
    val today = Calendar.getInstance()

    return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
}

fun Long.isSameDay(targetDate: Date): Boolean {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(this)) == formatter.format(targetDate)
}

fun Long.isTomorrow(): Boolean {
    val calendar = Calendar.getInstance().apply { timeInMillis = this@isTomorrow }
    val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }

    return calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)
}

fun Long.isAfterTomorrow(): Boolean {
    val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    val target = Calendar.getInstance().apply { timeInMillis = this@isAfterTomorrow }

    return target.after(tomorrow)
}

fun getTomorrowInMs(): Long {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun Long.formatToShortDayOfWeek(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("EEE", Locale.getDefault())
    return formatter.format(date)
}

fun Long.toReadableDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return format.format(date)
}

fun Long.toReadableDateShort(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    return format.format(date)
}

enum class TimeFormat {
    SHORT, FULL
}