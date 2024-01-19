package me.javahere.reachyourgoal.dto

import java.util.*

data class TaskAttachmentDto(
    val taskId: UUID,
    val fileName: String,
)