package me.javahere.reachyourgoal.dto

import java.util.*

data class TaskAttachmentDto(
    val id: UUID,
    val taskId: UUID,
    val fileName: String,
)