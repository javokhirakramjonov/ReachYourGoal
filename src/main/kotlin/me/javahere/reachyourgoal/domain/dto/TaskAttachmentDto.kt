package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.id.TaskAttachmentId
import me.javahere.reachyourgoal.domain.id.TaskId

class TaskAttachmentDto(
    val id: TaskAttachmentId,
    val taskId: TaskId,
    val fileName: String,
)
