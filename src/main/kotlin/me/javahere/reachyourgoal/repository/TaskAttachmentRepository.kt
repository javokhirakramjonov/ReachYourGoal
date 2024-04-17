package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskAttachment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskAttachmentRepository : CoroutineCrudRepository<TaskAttachment, Int> {
    fun findAllByTaskId(taskId: Int): Flow<TaskAttachment>

    suspend fun deleteAllByTaskId(taskId: Int)
}
