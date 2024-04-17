package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.Task
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskRepository : CoroutineCrudRepository<Task, Int> {
    suspend fun findByIdAndUserId(
        id: Int,
        userId: Int,
    ): Task?

    fun findAllByUserId(userId: Int): Flow<Task>
}
