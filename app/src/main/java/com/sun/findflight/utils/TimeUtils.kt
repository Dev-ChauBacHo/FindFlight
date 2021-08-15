package com.sun.findflight.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private const val EMPTY_STRING = ""
    private const val TIME_DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_DATE_HOUR_FORMAT = "yyyy-MM-dd HH:mm"
    private const val SECOND = 1000
    private const val HOUR = 60
    private const val DAY = 24
    private const val INVALID_TIME = -1L
    private const val CHAR_PREFIX = "PT"
    private const val CHAR_HOUR = "H"
    private const val CHAR_MINUTE = "M"

    fun updateTokenTime(tokenTime: Long?) =
        tokenTime?.let { System.currentTimeMillis() / BaseConst.SECONDS_VALUE + it }

    fun tokenExpired(tokenTime: Long?) =
        tokenTime?.let { it > System.currentTimeMillis() / BaseConst.SECONDS_VALUE } ?: true

    fun convertToDate(milliseconds: Long): String {
        val simpleDateFormat = SimpleDateFormat(TIME_DATE_FORMAT, Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        return simpleDateFormat.format(calendar.time)
    }

    fun calculateTimeGap(start: String, end: String): String {
        val millisecondsStart = convertToMilliseconds(start)
        val millisecondsEnd = convertToMilliseconds(end)
        if (millisecondsStart > INVALID_TIME && millisecondsEnd > INVALID_TIME) {
            val gap = millisecondsEnd - millisecondsStart
            return convertToHour(gap)
        }
        return EMPTY_STRING
    }

    private fun convertToMilliseconds(time: String) = try {
        val simpleDateFormat = SimpleDateFormat(TIME_DATE_HOUR_FORMAT, Locale.ENGLISH)
        val date = simpleDateFormat.parse(time)
        date?.time ?: INVALID_TIME
    } catch (e: ParseException) {
        INVALID_TIME
    }

    private fun convertToHour(milliseconds: Long): String {
        val minutes = (milliseconds / (SECOND * HOUR) % HOUR).toInt()
        val hours = (milliseconds / (SECOND * HOUR * HOUR) % DAY).toInt()
        return "$hours:$minutes"
    }

    fun convertTimeToMinute(time: String): Int {
        val newTime = time.removePrefix(CHAR_PREFIX).removeSuffix(CHAR_MINUTE)
        if (!time.contains(CHAR_HOUR)) {
            newTime.toIntOrNull().also { return it ?: 0 }
        }
        if (!time.contains(CHAR_MINUTE)) {
            newTime.removeSuffix(CHAR_HOUR).toIntOrNull().also {
                val totalMinute = it?.times(HOUR)
                return totalMinute ?: 0
            }
        }
        val unit = newTime.split(CHAR_HOUR)
        val hour = unit.first().toIntOrNull()
        val minute = unit.last().toIntOrNull()
        return if (hour != null && minute != null) hour * HOUR + minute else 0
    }
}
