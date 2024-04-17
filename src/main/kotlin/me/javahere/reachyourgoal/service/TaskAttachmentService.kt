package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.domain.id.TaskAttachmentId
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.UserId
import org.springframework.http.codec.multipart.FilePart
import java.io.File

interface TaskAttachmentService {
    suspend fun createTaskAttachment(
        taskId: TaskId,
        filePart: FilePart,
        userId: UserId,
    ): TaskAttachmentDto

    suspend fun getTaskAttachmentById(
        attachmentId: TaskAttachmentId,
        userId: UserId,
    ): Pair<TaskAttachmentName, File>

    suspend fun getAllTaskAttachmentsByTaskId(
        taskId: TaskId,
        userId: UserId,
    ): Flow<TaskAttachmentDto>

    suspend fun deleteTaskAttachmentById(
        attachmentId: TaskAttachmentId,
        userId: UserId,
    )

    suspend fun deleteAllTaskAttachmentsByTaskId(
        taskId: TaskId,
        userId: UserId,
    )

    suspend fun validateTaskAttachmentExistence(
        attachmentId: TaskAttachmentId,
        userId: UserId,
    ): TaskAttachmentDto
}
