package me.javahere.reachyourgoal.domain.dto.request

import com.fasterxml.jackson.annotation.JsonIgnore
import me.javahere.reachyourgoal.domain.DateFrequency
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.util.extensions.createListOfDays
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

open class RequestCreateTaskSchedule(
    @JsonIgnore
    val fromDate: LocalDate,
    @JsonIgnore
    val toDate: LocalDate,
    @JsonIgnore
    val frequency: DateFrequency,
    @JsonIgnore
    val time: LocalTime,
) {
    class CreateTaskDateSchedule(
        private val taskDate: LocalDate,
        private val taskTime: LocalTime = LocalTime.MIDNIGHT,
    ) : RequestCreateTaskSchedule(
            taskDate,
            taskDate,
            DateFrequency.Days(1),
            taskTime,
        )

    class CreateTaskDatesSchedule(
        private val taskFromDate: LocalDate,
        private val taskToDate: LocalDate,
        private val dayFrequency: Int,
        private val taskTime: LocalTime = LocalTime.MIDNIGHT,
    ) : RequestCreateTaskSchedule(
            taskFromDate,
            taskToDate,
            DateFrequency.Days(dayFrequency),
            taskTime,
        )

    class CreateTaskWeekDatesSchedule(
        private val taskFromDate: LocalDate,
        private val taskToDate: LocalDate,
        private val taskWeekDays: Set<DayOfWeek>,
        private val taskTime: LocalTime = LocalTime.MIDNIGHT,
    ) : RequestCreateTaskSchedule(
            taskFromDate,
            taskToDate,
            DateFrequency.WeekDays(taskWeekDays),
            taskTime,
        )

    fun transform(taskId: TaskId): List<TaskSchedule> {
        return createListOfDays(fromDate, toDate, frequency).map {
            TaskSchedule(
                taskId = taskId,
                taskDateTime = it.atTime(time),
            )
        }
    }
}
