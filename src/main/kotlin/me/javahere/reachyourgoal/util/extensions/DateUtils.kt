package me.javahere.reachyourgoal.util.extensions

import me.javahere.reachyourgoal.domain.entity.DateFrequency
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

const val WEEK_DAYS_COUNT = 7

fun createListOfDays(
    fromDate: LocalDate,
    toDate: LocalDate,
    dateFrequency: DateFrequency,
): List<LocalDate> {
    return when (dateFrequency) {
        is DateFrequency.Days -> createListOfDays(fromDate, toDate, dateFrequency.days)
        is DateFrequency.WeekDays -> createListOfDays(fromDate, toDate, dateFrequency.weekDays)
    }
}

fun createListOfDays(
    fromDate: LocalDate,
    toDate: LocalDate,
    frequency: Int,
): List<LocalDate> {
    val frequencyAsLong = frequency.toLong()
    val dates = mutableListOf<LocalDate>()
    var currentDate = fromDate

    while (!currentDate.isAfter(toDate)) {
        dates += currentDate

        currentDate = currentDate.plusDays(frequencyAsLong)
    }

    return dates.toList()
}

fun createListOfDays(
    fromDate: LocalDate,
    toDate: LocalDate,
    weekDays: Set<DayOfWeek>,
): List<LocalDate> {
    return weekDays.map {
        var currentDate = fromDate

        while (currentDate.dayOfWeek != it) {
            currentDate = currentDate.plusDays(1L)
        }

        createListOfDays(
            currentDate,
            toDate,
            WEEK_DAYS_COUNT,
        )
    }.flatten()
}

fun kotlin.time.Duration.toJavaDuration(): java.time.Duration {
    return java.time.Duration.ofMillis(this.inWholeMilliseconds)
}

fun java.time.Duration.toKotlinDuration(): kotlin.time.Duration {
    return this.toMillis().milliseconds
}
