package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import me.javahere.reachyourgoal.domain.entity.TaskStatus
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import java.time.LocalDate

class TaskScheduleDto(
    val scheduleId: TaskScheduleId,
    val taskId: TaskId,
    val taskPlanId: TaskPlanId,
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
