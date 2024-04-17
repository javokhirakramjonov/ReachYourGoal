package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.TaskStatus
import me.javahere.reachyourgoal.domain.id.TaskScheduleId

class RequestUpdateTaskSchedule(
    val scheduleId: TaskScheduleId,
    val taskStatus: TaskStatus,
)

class RequestUpdateTaskSchedules(
    val taskSchedules: List<RequestUpdateTaskSchedule>,
)
