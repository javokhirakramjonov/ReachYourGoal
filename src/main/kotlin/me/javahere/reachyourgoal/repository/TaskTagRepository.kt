package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskTag
import me.javahere.reachyourgoal.domain.id.TaskTagId
import me.javahere.reachyourgoal.domain.id.UserId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskTagRepository : CoroutineCrudRepository<TaskTag, TaskTagId> {
    suspend fun findByIdAndUserId(
        id: TaskTagId,
        userId: UserId,
    ): TaskTag?

    fun findAllByUserId(userId: UserId): Flow<TaskTag>

    suspend fun deleteAllByUserId(userId: UserId)

    suspend fun deleteByIdAndUserId(
        id: TaskTagId,
        userId: UserId,
    )
}
