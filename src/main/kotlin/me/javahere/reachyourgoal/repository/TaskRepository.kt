package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface TaskRepository : CoroutineCrudRepository<Task, UUID> {
    fun findAllByUserId(userId: UUID): Flow<Task>

    suspend fun findTaskByIdAndUserId(
        id: UUID,
        userId: UUID,
    ): Task?

    suspend fun deleteTaskByIdAndUserId(
        taskId: UUID,
        userId: UUID,
    )
}
