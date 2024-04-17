package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import org.springframework.http.codec.multipart.FilePart
import java.io.File

interface TaskAttachmentService {
    suspend fun createTaskAttachment(
        taskId: Int,
        filePart: FilePart,
        userId: Int,
    ): TaskAttachmentDto

    suspend fun getTaskAttachmentById(
        attachmentId: Int,
        userId: Int,
    ): Pair<TaskAttachmentName, File>

    suspend fun getAllTaskAttachmentsByTaskId(
        taskId: Int,
        userId: Int,
    ): Flow<TaskAttachmentDto>

    suspend fun deleteTaskAttachmentById(
        attachmentId: Int,
        userId: Int,
    )

    suspend fun deleteAllTaskAttachmentsByTaskId(
        taskId: Int,
        userId: Int,
    )

    suspend fun validateTaskAttachmentExistence(
        attachmentId: Int,
        userId: Int,
    ): TaskAttachmentDto
}
