package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.id.TaskScheduleId

class RequestDeleteTaskSchedules(
    val taskScheduleIds: List<TaskScheduleId>,
)
