package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.ScheduledTask
import me.javahere.reachyourgoal.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestScheduledTask
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import org.springframework.http.codec.multipart.FilePart
import java.io.File
import java.util.UUID

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
        filePart: FilePart,
    ): TaskAttachmentDto

    suspend fun getTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ): Pair<String, File>

    suspend fun getAllTaskAttachments(
        userId: UUID,
        taskId: UUID,
    ): Flow<TaskAttachmentDto>

    suspend fun deleteTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    )

    suspend fun addScheduledTasks(
        userId: UUID,
        taskId: UUID,
        requestScheduledTask: RequestScheduledTask,
    ): Flow<ScheduledTask>
}
