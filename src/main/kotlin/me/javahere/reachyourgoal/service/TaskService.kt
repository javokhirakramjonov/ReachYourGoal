package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import java.util.*

interface TaskService {
    suspend fun createTask(task: RequestTaskCreate, userId: UUID): TaskDto

    suspend fun getTaskByTaskIdAndUserId(id: UUID, userId: UUID): TaskDto
    fun getAllTasksByUserId(userId: UUID): Flow<TaskDto>

    suspend fun updateTask(task: TaskDto): TaskDto

    suspend fun deleteTaskByTaskIdAndUserId(taskId: UUID, userId: UUID)
}