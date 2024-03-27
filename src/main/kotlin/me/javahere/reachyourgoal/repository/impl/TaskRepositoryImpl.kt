package me.javahere.reachyourgoal.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import me.javahere.reachyourgoal.dao.TaskAttachmentDao
import me.javahere.reachyourgoal.dao.TaskDao
import me.javahere.reachyourgoal.dao.TaskSchedulingDao
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.domain.TaskScheduling
import me.javahere.reachyourgoal.repository.TaskRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskAttachmentDao: TaskAttachmentDao,
    private val taskSchedulingDao: TaskSchedulingDao,
) : TaskRepository {
    override suspend fun addTask(task: Task): Task {
        return taskDao.save(task)
    }

    override fun getAllTasks(userId: UUID): Flow<Task> {
        return taskDao.findAllByUserId(userId)
    }

    override suspend fun getTaskById(
        taskId: UUID,
        userId: UUID,
    ): Task? {
        return taskDao.findTaskByIdAndUserId(taskId, userId).firstOrNull()
    }

    override suspend fun updateTask(task: Task): Task {
        return taskDao.save(task)
    }

    override suspend fun deleteTaskById(
        taskId: UUID,
        userId: UUID,
    ) {
        taskDao.deleteTaskByIdAndUserId(taskId, userId)
    }

    override fun getAllTaskAttachments(taskId: UUID): Flow<TaskAttachment> {
        return taskAttachmentDao.findAllByTaskId(taskId)
    }

    override suspend fun addTaskAttachment(taskAttachment: TaskAttachment): TaskAttachment {
        return taskAttachmentDao.save(taskAttachment)
    }

    override suspend fun getTaskAttachmentById(
        attachmentId: UUID,
        taskId: UUID,
    ): TaskAttachment? {
        return taskAttachmentDao.findByIdAndTaskId(attachmentId, taskId).firstOrNull()
    }

    override suspend fun deleteTaskAttachmentById(
        taskAttachmentId: UUID,
        taskId: UUID,
    ) {
        taskAttachmentDao.deleteByIdAndTaskId(taskAttachmentId, taskId)
    }

    override fun addTaskScheduling(taskScheduling: List<TaskScheduling>): Flow<TaskScheduling> {
        return taskSchedulingDao.saveAll(taskScheduling)
    }

    override suspend fun getTaskSchedulingForPeriod(
        taskId: UUID,
        fromDateTime: LocalDateTime,
        toDateTime: LocalDateTime,
    ): Flow<TaskScheduling> {
        return taskSchedulingDao.findByTaskIdAndTaskDateTimeBetween(taskId, fromDateTime, toDateTime)
    }

    override suspend fun getTaskSchedulingById(schedulingId: Long): TaskScheduling? {
        return taskSchedulingDao.findById(schedulingId)
    }

    override suspend fun deleteTaskSchedulingForDateTimes(
        taskId: UUID,
        dateTimes: List<LocalDateTime>,
    ) {
        taskSchedulingDao.deleteByTaskIdAndTaskDateTimeIn(taskId, dateTimes)
    }

    override suspend fun updateTaskScheduling(taskScheduling: TaskScheduling): TaskScheduling {
        return taskSchedulingDao.save(taskScheduling)
    }
}
