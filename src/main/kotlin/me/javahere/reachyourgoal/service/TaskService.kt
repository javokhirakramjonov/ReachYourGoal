package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskSchedule
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

    suspend fun addTaskSchedule(
        userId: UUID,
        taskId: UUID,
        requestCreateTaskSchedule: RequestCreateTaskSchedule,
    ): Flow<TaskScheduleDto>

    suspend fun getTaskSchedule(
        userId: UUID,
        taskId: UUID,
        requestTaskSchedule: RequestGetTaskSchedule,
    ): Flow<TaskScheduleDto>

    suspend fun deleteTaskSchedule(
        userId: UUID,
        taskId: UUID,
        taskSchedule: RequestCreateTaskSchedule,
    )

    suspend fun updateTaskStatus(
        userId: UUID,
        requestUpdateTaskStatus: RequestUpdateTaskStatus,
    ): TaskScheduleDto
}
