package me.javahere.reachyourgoal.datasource.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.domain.ScheduledTask
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.repository.ScheduledTaskRepository
import me.javahere.reachyourgoal.repository.TaskAttachmentRepository
import me.javahere.reachyourgoal.repository.TaskRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TaskDataSourceImpl(
    private val taskRepository: TaskRepository,
    private val taskAttachmentRepository: TaskAttachmentRepository,
    private val scheduledTaskRepository: ScheduledTaskRepository,
) : TaskDataSource {
    override suspend fun createTask(task: Task): Task {
        return taskRepository.save(task)
    }

    override fun retrieveAllTasks(userId: UUID): Flow<Task> {
        return taskRepository.findAllByUserId(userId)
    }

    override suspend fun retrieveTaskById(
        taskId: UUID,
        userId: UUID,
    ): Task? {
        return taskRepository.findTaskByIdAndUserId(taskId, userId)
    }

    override suspend fun updateTask(task: Task): Task {
        return taskRepository.save(task)
    }

    override suspend fun deleteTaskById(
        taskId: UUID,
        userId: UUID,
    ) {
        taskRepository.deleteTaskByIdAndUserId(taskId, userId)
    }

    override fun retrieveAllTaskAttachments(taskId: UUID): Flow<TaskAttachment> {
        return taskAttachmentRepository.findAllByTaskId(taskId)
    }

    override suspend fun createTaskAttachment(taskAttachment: TaskAttachment): TaskAttachment {
        return taskAttachmentRepository.save(taskAttachment)
    }

    override suspend fun retrieveTaskAttachmentById(
        attachmentId: UUID,
        taskId: UUID,
    ): TaskAttachment? {
        return taskAttachmentRepository.findByIdAndTaskId(attachmentId, taskId)
    }

    override suspend fun deleteTaskAttachmentById(
        taskAttachmentId: UUID,
        taskId: UUID,
    ) {
        taskAttachmentRepository.deleteByIdAndTaskId(taskAttachmentId, taskId)
    }

    override suspend fun addScheduledTask(scheduledTasks: List<ScheduledTask>): Flow<ScheduledTask> {
        return scheduledTaskRepository.saveAll(scheduledTasks)
    }
}
