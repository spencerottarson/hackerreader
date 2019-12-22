package com.ottarson.hackerreader.utils

import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

fun Date.getTimePast(): String {
    val number: Long
    var unit: String

    val now = Date()
    val days = TimeUnit.MILLISECONDS.toDays(now.time - this.time)
    val years = days / 365
    val months = days / 30
    val hours = TimeUnit.MILLISECONDS.toHours(now.time - this.time)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - this.time)

    when {
        years > 0 -> {
            number = years
            unit = "year"
        }
        months > 0 -> {
            number = months
            unit = "month"
        }
        days > 0 -> {
            number = days
            unit = "day"
        }
        hours > 0 -> {
            number = hours
            unit = "hour"
        }
        minutes > 0 -> {
            number = minutes
            unit = "minute"
        }
        else -> return "a moment ago"
    }

    if (number > 1) {
        unit += "s"
    }

    return "$number $unit ago"
}

fun Date.addTime(amount: Int, timeUnit: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(timeUnit, amount)
    return calendar.time
}
