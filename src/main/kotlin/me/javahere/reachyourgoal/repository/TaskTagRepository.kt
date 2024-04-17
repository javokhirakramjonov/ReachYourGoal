package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskTag
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskTagRepository : CoroutineCrudRepository<TaskTag, Int> {
    suspend fun findByIdAndUserId(
        id: Int,
        userId: Int,
    ): TaskTag?

    fun findAllByUserId(userId: Int): Flow<TaskTag>

    suspend fun deleteAllByUserId(userId: Int)

    suspend fun deleteByIdAndUserId(
        id: Int,
        userId: Int,
    )
}
