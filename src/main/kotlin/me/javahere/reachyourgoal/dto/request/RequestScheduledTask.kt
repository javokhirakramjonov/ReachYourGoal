package me.javahere.reachyourgoal.dto.request

import me.javahere.reachyourgoal.dto.DateFrequency
import me.javahere.reachyourgoal.util.Transformable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

open class RequestScheduledTask(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val frequency: DateFrequency,
    val time: LocalTime?,
)

data class RequestTaskDate(
    private val taskDate: LocalDate,
    private val taskTime: LocalTime? = null,
) : Transformable<RequestScheduledTask> {
    override fun transform(): RequestScheduledTask {
        return RequestScheduledTask(
            taskDate,
            taskDate,
            DateFrequency.Days(1),
            taskTime,
        )
    }
}

data class RequestTaskDates(
    private val taskFromDate: LocalDate,
    private val taskToDate: LocalDate,
    private val dayFrequency: Int,
    private val taskTime: LocalTime? = null,
) : Transformable<RequestScheduledTask> {
    override fun transform(): RequestScheduledTask {
        return RequestScheduledTask(
            taskFromDate,
            taskToDate,
            DateFrequency.Days(dayFrequency),
            taskTime,
        )
    }
}

data class RequestTaskWeekDates(
    private val taskFromDate: LocalDate,
    private val taskToDate: LocalDate,
    private val taskWeekDays: Set<DayOfWeek>,
    private val taskTime: LocalTime? = null,
) : Transformable<RequestScheduledTask> {
    override fun transform(): RequestScheduledTask {
        return RequestScheduledTask(
            taskFromDate,
            taskToDate,
            DateFrequency.WeekDays(taskWeekDays),
            taskTime,
        )
    }
}
