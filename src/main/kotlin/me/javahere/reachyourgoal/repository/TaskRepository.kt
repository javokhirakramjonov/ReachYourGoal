package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.domain.TaskSchedule
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

    fun addTaskSchedule(taskSchedule: List<TaskSchedule>): Flow<TaskSchedule>

    suspend fun getTaskScheduleForPeriod(
        taskId: UUID,
        fromDateTime: LocalDateTime,
        toDateTime: LocalDateTime,
    ): Flow<TaskSchedule>

    suspend fun getTaskScheduleById(scheduleId: Long): TaskSchedule?

    suspend fun deleteTaskScheduleForDateTimes(
        taskId: UUID,
        dateTimes: List<LocalDateTime>,
    )

    suspend fun updateTaskSchedule(taskSchedule: TaskSchedule): TaskSchedule
}
