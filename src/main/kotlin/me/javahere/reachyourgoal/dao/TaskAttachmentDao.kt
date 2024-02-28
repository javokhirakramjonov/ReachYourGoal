package me.javahere.reachyourgoal.dao

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.TaskAttachment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface TaskAttachmentDao : CoroutineCrudRepository<TaskAttachment, UUID> {
    fun findAllByTaskId(taskId: UUID): Flow<TaskAttachment>

    suspend fun findByIdAndTaskId(
        attachmentId: UUID,
        taskId: UUID,
    ): TaskAttachment?

    suspend fun deleteByIdAndTaskId(
        attachmentId: UUID,
        taskId: UUID,
    )
}
