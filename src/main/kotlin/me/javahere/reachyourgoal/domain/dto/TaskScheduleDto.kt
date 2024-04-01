package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.TaskStatus
import java.time.LocalDateTime
import java.util.UUID

class TaskScheduleDto(
    val scheduleId: Long,
    val taskId: UUID,
    val taskDateTime: LocalDateTime,
    val taskStatus: TaskStatus = TaskStatus.NOT_STARTED,
)
