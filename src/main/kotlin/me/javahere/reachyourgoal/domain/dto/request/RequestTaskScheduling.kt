package me.javahere.reachyourgoal.domain.dto.request

import com.fasterxml.jackson.annotation.JsonIgnore
import me.javahere.reachyourgoal.domain.dto.DateFrequency
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

open class RequestTaskScheduling(
    @JsonIgnore
    val fromDate: LocalDate,
    @JsonIgnore
    val toDate: LocalDate,
    @JsonIgnore
    val frequency: DateFrequency,
    @JsonIgnore
    val time: LocalTime?,
) {
    class TaskDateScheduling(
        private val taskDate: LocalDate,
        private val taskTime: LocalTime? = null,
    ) : RequestTaskScheduling(
            taskDate,
            taskDate,
            DateFrequency.Days(1),
            taskTime,
        )

    class TaskDatesScheduling(
        private val taskFromDate: LocalDate,
        private val taskToDate: LocalDate,
        private val dayFrequency: Int,
        private val taskTime: LocalTime? = null,
    ) : RequestTaskScheduling(
            taskFromDate,
            taskToDate,
            DateFrequency.Days(dayFrequency),
            taskTime,
        )

    class TaskWeekDatesScheduling(
        private val taskFromDate: LocalDate,
        private val taskToDate: LocalDate,
        private val taskWeekDays: Set<DayOfWeek>,
        private val taskTime: LocalTime? = null,
    ) : RequestTaskScheduling(
            taskFromDate,
            taskToDate,
            DateFrequency.WeekDays(taskWeekDays),
            taskTime,
        )
}
