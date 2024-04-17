package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import me.javahere.reachyourgoal.domain.entity.TaskStatus
import java.time.LocalDate

class TaskScheduleDto(
    val scheduleId: Int,
    val taskId: Int,
    val taskPlanId: Int,
    val taskDate: LocalDate,
    val taskStatus: TaskStatus = TaskStatus.NOT_STARTED,
) : Transformable<TaskSchedule> {
    override fun transform(): TaskSchedule {
        return TaskSchedule(
            scheduleId = scheduleId,
            taskId = taskId,
            taskPlanId = taskPlanId,
            taskDate = taskDate,
            taskStatus = taskStatus,
        )
    }
}
