package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import org.springframework.core.io.buffer.DataBuffer
import java.io.File
import java.util.*

interface TaskService {
    suspend fun createTask(
        task: RequestTaskCreate,
        userId: UUID,
    ): TaskDto

    suspend fun getTaskById(
        taskId: UUID,
        userId: UUID,
    ): TaskDto

    fun getAllTasks(userId: UUID): Flow<TaskDto>

    suspend fun updateTask(task: TaskDto): TaskDto

    suspend fun deleteTaskById(
        taskId: UUID,
        userId: UUID,
    )

    suspend fun createTaskAttachment(
        userId: UUID,
        taskId: UUID,
        attachments: List<Pair<String, DataBuffer>>,
    ): List<Pair<String, TaskAttachmentDto?>>

    suspend fun getTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ): File

    suspend fun getAllTaskAttachments(
        userId: UUID,
        taskId: UUID,
    ): Flow<TaskAttachmentDto>

    suspend fun deleteTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    )
}
