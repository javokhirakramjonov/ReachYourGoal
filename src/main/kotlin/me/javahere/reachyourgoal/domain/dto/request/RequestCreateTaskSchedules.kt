package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import java.time.LocalDate

class RequestCreateTaskSchedules(
    val taskId: TaskId,
    val planId: TaskPlanId,
    val taskDates: List<LocalDate>,
)
