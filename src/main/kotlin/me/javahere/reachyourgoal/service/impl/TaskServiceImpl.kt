package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.domain.TaskScheduling
import me.javahere.reachyourgoal.domain.TaskSchedulingId
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestTaskScheduling
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.repository.TaskRepository
import me.javahere.reachyourgoal.service.FileService
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.createListOfDays
import me.javahere.reachyourgoal.util.transformCollection
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.LocalDate
import java.util.UUID

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val fileService: FileService,
    @Value("\${app.task-file-path}") private val taskFilePath: String,
) : TaskService {
    override suspend fun createTask(
        task: RequestCreateTask,
        userId: UUID,
    ): TaskDto {
        return taskRepository
            .addTask(task.transform(userId))
            .transform()
    }

    override suspend fun getTaskById(
        taskId: UUID,
        userId: UUID,
    ): TaskDto {
        return validateTaskExistence(userId, taskId).transform()
    }

    override fun getAllTasks(userId: UUID): Flow<TaskDto> {
        return taskRepository
            .getAllTasks(userId)
            .transformCollection()
    }

    override suspend fun updateTask(task: TaskDto): TaskDto {
        return taskRepository
            .updateTask(task.transform())
            .transform()
    }

    override suspend fun deleteTaskById(
        taskId: UUID,
        userId: UUID,
    ) {
        taskRepository.deleteTaskById(taskId, userId)
    }

    @Transactional
    override suspend fun createTaskAttachment(
        userId: UUID,
        taskId: UUID,
        filePart: FilePart,
    ): TaskAttachmentDto {
        validateTaskExistence(userId, taskId)

        val taskAttachment =
            TaskAttachment(
                name = filePart.filename(),
                taskId = taskId,
            )

        val createdTaskAttachment = taskRepository.addTaskAttachment(taskAttachment).transform()

        val newName = createdTaskAttachment.id.toString()

        val isSaved = fileService.createFile(taskFilePath, newName, filePart)

        return if (isSaved) createdTaskAttachment else throw RYGException()
    }

    override suspend fun getTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ): Pair<String, File> {
        val attachment = validateTaskAttachmentExistence(userId, taskId, attachmentId).transform()

        val attachmentFile =
            fileService.getFileByName(taskFilePath, attachment.id.toString())
                ?: throw RYGException("File(id = ${attachment.id}) not found for attachment(id = $attachmentId)")

        return attachment.fileName to attachmentFile
    }

    override suspend fun getAllTaskAttachments(
        userId: UUID,
        taskId: UUID,
    ): Flow<TaskAttachmentDto> {
        validateTaskExistence(userId, taskId)

        return taskRepository.getAllTaskAttachments(taskId).transformCollection()
    }

    override suspend fun deleteTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ) {
        val attachment = validateTaskAttachmentExistence(userId, taskId, attachmentId).transform()

        fileService.deleteFileByName(taskFilePath, attachment.id.toString())
        taskRepository.deleteTaskAttachmentById(attachmentId, taskId)
    }

    override suspend fun addTaskScheduling(
        userId: UUID,
        taskId: UUID,
        requestTaskScheduling: RequestTaskScheduling,
    ): Flow<TaskScheduling> {
        validateTaskExistence(userId, taskId)

        val taskScheduling =
            createListOfDays(
                requestTaskScheduling.fromDate,
                requestTaskScheduling.toDate,
                requestTaskScheduling.frequency,
            ).map {
                TaskScheduling(
                    taskId,
                    it,
                    requestTaskScheduling.time,
                )
            }

        return taskRepository.addTaskScheduling(taskScheduling)
    }

    override suspend fun getTaskScheduling(
        userId: UUID,
        taskId: UUID,
        fromDate: LocalDate,
        toDate: LocalDate,
    ): Flow<TaskScheduling> {
        validateTaskExistence(userId, taskId)
        return taskRepository.getTaskScheduling(taskId, fromDate, toDate)
    }

    private suspend fun validateTaskExistence(
        userId: UUID,
        taskId: UUID,
    ): Task {
        return taskRepository
            .getTaskById(taskId, userId)
            ?: throw RYGException("Task not found with such id($taskId) for userId($userId)")
    }

    private suspend fun validateTaskAttachmentExistence(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ): TaskAttachment {
        validateTaskExistence(userId, taskId)

        return taskRepository
            .getTaskAttachmentById(attachmentId, taskId)
            ?: throw RYGException("Attachment(id = $attachmentId) not found for task(id = $taskId)")
    }

    override suspend fun deleteTaskScheduling(
        userId: UUID,
        taskId: UUID,
        taskScheduling: RequestTaskScheduling,
    ) {
        validateTaskExistence(userId, taskId)

        val taskSchedulingIds =
            createListOfDays(
                taskScheduling.fromDate,
                taskScheduling.toDate,
                taskScheduling.frequency,
            ).map {
                TaskSchedulingId(
                    taskId = taskId,
                    taskDate = it,
                    taskTime = taskScheduling.time,
                )
            }

        taskRepository.deleteTaskSchedulingById(taskSchedulingIds)
    }
}
