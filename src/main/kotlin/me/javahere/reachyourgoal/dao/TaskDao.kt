package me.javahere.reachyourgoal.dao

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface TaskDao : CoroutineCrudRepository<Task, UUID> {
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
