package me.javahere.reachyourgoal.domain

import java.time.DayOfWeek

sealed class DateFrequency {
    class Days(val days: Int) : DateFrequency()

    class WeekDays(val weekDays: Set<DayOfWeek>) : DateFrequency()
}
