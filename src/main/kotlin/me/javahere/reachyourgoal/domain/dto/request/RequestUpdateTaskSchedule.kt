package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.TaskStatus

class RequestUpdateTaskSchedule(
    val scheduleId: Int,
    val taskStatus: TaskStatus,
)

class RequestUpdateTaskSchedules(
    val taskSchedules: List<RequestUpdateTaskSchedule>,
)
