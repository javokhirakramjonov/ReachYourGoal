package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.TaskAttachment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface TaskAttachmentRepository : CoroutineCrudRepository<TaskAttachment, UUID> {
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
