package me.javahere.reachyourgoal.datasource

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import java.util.*

interface TaskDataSource {
    suspend fun createTask(task: Task): Task

    fun retrieveAllTasksByUserId(userId: UUID): Flow<Task>
    suspend fun retrieveTaskByTaskIdAndUserId(taskId: UUID, userId: UUID): Task?

    suspend fun updateTask(task: Task): Task

    suspend fun deleteTaskByTaskIdAndUserId(taskId: UUID, userId: UUID)
}