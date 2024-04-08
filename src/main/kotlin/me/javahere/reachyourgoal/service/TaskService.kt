package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask

typealias TaskAttachmentName = String

interface TaskService {
    suspend fun createTask(
        requestCreateTask: RequestCreateTask,
        userId: Int,
    ): TaskDto

    suspend fun getTaskById(
        taskId: Int,
        userId: Int,
    ): TaskDto

    suspend fun getAllTasksByCategoryId(
        categoryId: Int,
        userId: Int,
    ): Flow<TaskDto>

    suspend fun updateTask(
        task: TaskDto,
        userId: Int,
    ): TaskDto

    suspend fun deleteTaskById(
        taskId: Int,
        userId: Int,
    )

    suspend fun validateTaskExistence(
        taskId: Int,
        userId: Int,
    ): TaskDto
}
