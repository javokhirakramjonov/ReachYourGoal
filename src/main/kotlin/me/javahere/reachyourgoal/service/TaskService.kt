package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.TaskScheduling
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestTaskScheduling
import org.springframework.http.codec.multipart.FilePart
import java.io.File
import java.time.LocalDate
import java.util.UUID

interface TaskService {
    suspend fun createTask(
        task: RequestCreateTask,
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

    suspend fun addTaskScheduling(
        userId: UUID,
        taskId: UUID,
        requestTaskScheduling: RequestTaskScheduling,
    ): Flow<TaskScheduling>

    suspend fun getTaskScheduling(
        userId: UUID,
        taskId: UUID,
        fromDate: LocalDate,
        toDate: LocalDate,
    ): Flow<TaskScheduling>

    suspend fun deleteTaskScheduling(
        userId: UUID,
        taskId: UUID,
        taskScheduling: RequestTaskScheduling,
    )
}
