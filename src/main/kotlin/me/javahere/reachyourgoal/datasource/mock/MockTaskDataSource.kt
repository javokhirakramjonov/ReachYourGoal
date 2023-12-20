package me.javahere.reachyourgoal.datasource.mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.domain.Task
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MockTaskDataSource : TaskDataSource {

    private val tasks = mutableListOf<Task>()

    override suspend fun createTask(task: Task): Task {
        val taskWithId = if (task.id == null) task.copy(id = UUID.randomUUID()) else task
        tasks.add(taskWithId)
        return taskWithId
    }

    override fun retrieveAllTasksByUserId(userId: UUID): Flow<Task> {
        return tasks
            .asFlow()
            .filter {
                it.userId == userId
            }
    }

    override suspend fun retrieveTaskByTaskIdAndUserId(taskId: UUID, userId: UUID): Task? {
        return tasks.firstOrNull { it.id == taskId && it.userId == userId }
    }

    override suspend fun updateTask(task: Task): Task {
        tasks.replaceAll { if (it.id == task.id && it.userId == task.userId) task else it }
        return task
    }

    override suspend fun deleteTaskByTaskIdAndUserId(taskId: UUID, userId: UUID) {
        tasks.removeIf { it.id == taskId && it.userId == userId }
    }

}