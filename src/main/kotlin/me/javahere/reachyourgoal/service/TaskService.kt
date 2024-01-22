package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import org.springframework.core.io.buffer.DataBuffer
import java.io.File
import java.util.*

interface TaskService {
    suspend fun createTask(task: RequestTaskCreate, userId: UUID): TaskDto

    suspend fun getTaskByTaskIdAndUserId(id: UUID, userId: UUID): TaskDto
    fun getAllTasksByUserId(userId: UUID): Flow<TaskDto>

    suspend fun updateTask(task: TaskDto): TaskDto

    suspend fun deleteTaskByTaskIdAndUserId(taskId: UUID, userId: UUID)

    suspend fun createTaskAttachments(
        userId: UUID,
        taskId: UUID,
        attachments: List<Pair<String, DataBuffer>>
    ): List<Pair<String, TaskAttachmentDto?>>

    suspend fun getAttachment(userId: UUID, taskId: UUID, attachmentId: UUID): File

    suspend fun getAllAttachmentsByUserIdAndTaskId(userId: UUID, taskId: UUID): Flow<TaskAttachmentDto>
    suspend fun deleteTaskAttachmentByTaskIdAndAttachmentId(userId: UUID, taskId: UUID, attachmentId: UUID)
}