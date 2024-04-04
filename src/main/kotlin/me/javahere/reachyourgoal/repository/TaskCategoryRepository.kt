package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskCategory
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskCategoryRepository : CoroutineCrudRepository<TaskCategory, Int> {
    fun findByIdAndUserId(
        id: Int,
        userId: Int,
    ): Flow<TaskCategory>

    fun findAllByUserId(userId: Int): Flow<TaskCategory>

    suspend fun deleteByIdAndUserId(
        id: Int,
        userId: Int,
    )
}
