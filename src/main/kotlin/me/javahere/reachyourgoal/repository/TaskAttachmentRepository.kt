package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskAttachment
import me.javahere.reachyourgoal.domain.id.TaskAttachmentId
import me.javahere.reachyourgoal.domain.id.TaskId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskAttachmentRepository : CoroutineCrudRepository<TaskAttachment, TaskAttachmentId> {
    fun findAllByTaskId(taskId: TaskId): Flow<TaskAttachment>

    suspend fun deleteAllByTaskId(taskId: TaskId)
}
