package me.javahere.reachyourgoal.dto

import java.util.UUID

class TaskAttachmentDto(
    val id: UUID,
    val taskId: UUID,
    val fileName: String,
)
