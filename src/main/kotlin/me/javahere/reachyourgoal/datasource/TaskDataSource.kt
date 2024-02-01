package me.javahere.reachyourgoal.datasource

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import java.util.UUID

interface TaskDataSource {
    suspend fun createTask(task: Task): Task

    fun retrieveAllTasks(userId: UUID): Flow<Task>

    suspend fun retrieveTaskById(
        taskId: UUID,
        userId: UUID,
    ): Task?

    suspend fun updateTask(task: Task): Task

    suspend fun deleteTaskById(
        taskId: UUID,
        userId: UUID,
    )

    fun retrieveAllTaskAttachments(taskId: UUID): Flow<TaskAttachment>

    suspend fun createTaskAttachment(taskAttachment: TaskAttachment): TaskAttachment

    suspend fun retrieveTaskAttachmentById(
        attachmentId: UUID,
        taskId: UUID,
    ): TaskAttachment?

    suspend fun deleteTaskAttachmentById(
        taskAttachmentId: UUID,
        taskId: UUID,
    )
}
