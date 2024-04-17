package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.Task
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.UserId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskRepository : CoroutineCrudRepository<Task, TaskId> {
    suspend fun findByIdAndUserId(
        id: TaskId,
        userId: UserId,
    ): Task?

    fun findAllByUserId(userId: UserId): Flow<Task>
}
