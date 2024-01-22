package me.javahere.reachyourgoal.datasource.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.repository.TaskAttachmentRepository
import me.javahere.reachyourgoal.repository.TaskRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TaskDataSourceImpl(
    private val taskRepository: TaskRepository,
    private val taskAttachmentRepository: TaskAttachmentRepository
) : TaskDataSource {

    override suspend fun createTask(task: Task): Task {
        return taskRepository.save(task)
    }

    override fun retrieveAllTasksByUserId(userId: UUID): Flow<Task> {
        return taskRepository.findAllByUserId(userId)
    }

    override suspend fun retrieveTaskByTaskIdAndUserId(taskId: UUID, userId: UUID): Task? {
        return taskRepository.findTaskByIdAndUserId(taskId, userId)
    }

    override suspend fun updateTask(task: Task): Task {
        return taskRepository.save(task)
    }

    override suspend fun deleteTaskByTaskIdAndUserId(taskId: UUID, userId: UUID) {
        taskRepository.deleteTaskByIdAndUserId(taskId, userId)
    }

    override fun retrieveAllTaskAttachmentsByTaskId(taskId: UUID): Flow<TaskAttachment> {
        return taskAttachmentRepository.findAllByTaskId(taskId)
    }

    override suspend fun createTaskAttachment(taskAttachment: TaskAttachment): TaskAttachment {
        return taskAttachmentRepository.save(taskAttachment)
    }

    override suspend fun retrieveTaskAttachment(attachmentId: UUID, taskId: UUID): TaskAttachment? {
        return taskAttachmentRepository.findByIdAndTaskId(attachmentId, taskId)
    }

    override suspend fun deleteTaskAttachmentByAttachmentIdAndTaskId(taskAttachmentId: UUID, taskId: UUID) {
        taskAttachmentRepository.deleteByIdAndTaskId(taskAttachmentId, taskId)
    }
}