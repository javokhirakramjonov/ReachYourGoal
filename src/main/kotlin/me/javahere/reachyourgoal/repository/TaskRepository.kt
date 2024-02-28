package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.domain.TaskScheduling
import me.javahere.reachyourgoal.domain.TaskSchedulingId
import java.time.LocalDate
import java.util.UUID

interface TaskRepository {
    suspend fun addTask(task: Task): Task

    fun getAllTasks(userId: UUID): Flow<Task>

    suspend fun getTaskById(
        taskId: UUID,
        userId: UUID,
    ): Task?

    suspend fun updateTask(task: Task): Task

    suspend fun deleteTaskById(
        taskId: UUID,
        userId: UUID,
    )

    fun getAllTaskAttachments(taskId: UUID): Flow<TaskAttachment>

    suspend fun addTaskAttachment(taskAttachment: TaskAttachment): TaskAttachment

    suspend fun getTaskAttachmentById(
        attachmentId: UUID,
        taskId: UUID,
    ): TaskAttachment?

    suspend fun deleteTaskAttachmentById(
        taskAttachmentId: UUID,
        taskId: UUID,
    )

    fun addTaskScheduling(taskScheduling: List<TaskScheduling>): Flow<TaskScheduling>

    suspend fun getTaskScheduling(
        taskId: UUID,
        fromDate: LocalDate,
        toDate: LocalDate,
    ): Flow<TaskScheduling>

    suspend fun deleteTaskSchedulingById(taskSchedulingIds: List<TaskSchedulingId>)
}
