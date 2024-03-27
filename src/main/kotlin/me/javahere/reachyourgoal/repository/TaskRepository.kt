package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.domain.TaskScheduling
import java.time.LocalDateTime
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

    suspend fun getTaskSchedulingForPeriod(
        taskId: UUID,
        fromDateTime: LocalDateTime,
        toDateTime: LocalDateTime,
    ): Flow<TaskScheduling>

    suspend fun getTaskSchedulingById(schedulingId: Long): TaskScheduling?

    suspend fun deleteTaskSchedulingForDateTimes(
        taskId: UUID,
        dateTimes: List<LocalDateTime>,
    )

    suspend fun updateTaskScheduling(taskScheduling: TaskScheduling): TaskScheduling
}
