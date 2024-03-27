package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.TaskStatus
import java.util.UUID

class RequestUpdateTaskStatus(
    val taskId: UUID,
    val taskSchedulingId: Long,
    val taskStatus: TaskStatus,
)
