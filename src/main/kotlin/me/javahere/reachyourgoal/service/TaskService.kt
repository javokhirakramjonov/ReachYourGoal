package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.TaskSchedulingDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskScheduling
import me.javahere.reachyourgoal.domain.dto.request.RequestTaskScheduling
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateTaskStatus
import org.springframework.http.codec.multipart.FilePart
import java.io.File
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
    ): Flow<TaskSchedulingDto>

    suspend fun getTaskScheduling(
        userId: UUID,
        taskId: UUID,
        requestTaskScheduling: RequestGetTaskScheduling,
    ): Flow<TaskSchedulingDto>

    suspend fun deleteTaskScheduling(
        userId: UUID,
        taskId: UUID,
        taskScheduling: RequestTaskScheduling,
    )

    suspend fun updateTaskStatus(
        userId: UUID,
        requestUpdateTaskStatus: RequestUpdateTaskStatus,
    ): TaskSchedulingDto
}
