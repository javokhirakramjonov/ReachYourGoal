package me.javahere.reachyourgoal.dto.request

import com.fasterxml.jackson.annotation.JsonIgnore
import me.javahere.reachyourgoal.dto.DateFrequency
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

open class RequestScheduledTask(
    @JsonIgnore
    val fromDate: LocalDate,
    @JsonIgnore
    val toDate: LocalDate,
    @JsonIgnore
    val frequency: DateFrequency,
    @JsonIgnore
    val time: LocalTime?,
) {
    class RequestTaskDate(
        private val taskDate: LocalDate,
        private val taskTime: LocalTime? = null,
    ) : RequestScheduledTask(
            taskDate,
            taskDate,
            DateFrequency.Days(1),
            taskTime,
        )

    class RequestTaskDates(
        private val taskFromDate: LocalDate,
        private val taskToDate: LocalDate,
        private val dayFrequency: Int,
        private val taskTime: LocalTime? = null,
    ) : RequestScheduledTask(
            taskFromDate,
            taskToDate,
            DateFrequency.Days(dayFrequency),
            taskTime,
        )

    class RequestTaskWeekDates(
        private val taskFromDate: LocalDate,
        private val taskToDate: LocalDate,
        private val taskWeekDays: Set<DayOfWeek>,
        private val taskTime: LocalTime? = null,
    ) : RequestScheduledTask(
            taskFromDate,
            taskToDate,
            DateFrequency.WeekDays(taskWeekDays),
            taskTime,
        )
}
