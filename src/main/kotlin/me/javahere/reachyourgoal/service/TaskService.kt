package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.UserId

typealias TaskAttachmentName = String

interface TaskService {
    suspend fun createTask(
        requestCreateTask: RequestCreateTask,
        userId: UserId,
    ): TaskDto

    suspend fun getTaskById(
        taskId: TaskId,
        userId: UserId,
    ): TaskDto

    suspend fun getAllTasksByUserId(userId: UserId): Flow<TaskDto>

    suspend fun updateTask(
        task: TaskDto,
        userId: UserId,
    ): TaskDto

    suspend fun deleteTaskById(
        taskId: TaskId,
        userId: UserId,
    )

    suspend fun validateTaskExistence(
        taskId: TaskId,
        userId: UserId,
    ): TaskDto
}
