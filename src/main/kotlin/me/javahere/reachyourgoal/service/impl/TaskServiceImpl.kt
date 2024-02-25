package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.domain.ScheduledTask
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestScheduledTask
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType
import me.javahere.reachyourgoal.localize.MessagesEnum
import me.javahere.reachyourgoal.service.FileService
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.createListOfDays
import me.javahere.reachyourgoal.util.getMessage
import me.javahere.reachyourgoal.util.transformCollection
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.util.UUID

@Service
class TaskServiceImpl(
    private val taskDataSource: TaskDataSource,
    private val messageSource: ResourceBundleMessageSource,
    private val fileService: FileService,
    @Value("\${app.task-file-path}") private val taskFilePath: String,
) : TaskService {
    override suspend fun createTask(
        task: RequestTaskCreate,
        userId: UUID,
    ): TaskDto {
        return taskDataSource
            .createTask(task.transform(userId))
            .transform()
    }

    override suspend fun getTaskById(
        taskId: UUID,
        userId: UUID,
    ): TaskDto {
        return validateTaskExistence(userId, taskId).transform()
    }

    override fun getAllTasks(userId: UUID): Flow<TaskDto> {
        return taskDataSource
            .retrieveAllTasks(userId)
            .transformCollection()
    }

    override suspend fun updateTask(task: TaskDto): TaskDto {
        return taskDataSource
            .updateTask(task.transform())
            .transform()
    }

    override suspend fun deleteTaskById(
        taskId: UUID,
        userId: UUID,
    ) {
        taskDataSource.deleteTaskById(taskId, userId)
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

        val createdTaskAttachment = taskDataSource.createTaskAttachment(taskAttachment).transform()

        val newName = createdTaskAttachment.id.toString()

        val isSaved = fileService.createFile(taskFilePath, newName, filePart)

        return if (isSaved) createdTaskAttachment else throw RYGException(RYGExceptionType.INTERNAL_ERROR)
    }

    override suspend fun getTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ): Pair<String, File> {
        val attachment = validateTaskAttachmentExistence(userId, taskId, attachmentId).transform()

        val attachmentFile =
            fileService.getFileByName(taskFilePath, attachment.id.toString())
                ?: throw RYGException(RYGExceptionType.NOT_FOUND)

        return attachment.fileName to attachmentFile
    }

    override suspend fun getAllTaskAttachments(
        userId: UUID,
        taskId: UUID,
    ): Flow<TaskAttachmentDto> {
        validateTaskExistence(userId, taskId)

        return taskDataSource.retrieveAllTaskAttachments(taskId).transformCollection()
    }

    override suspend fun deleteTaskAttachmentById(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ) {
        val attachment = validateTaskAttachmentExistence(userId, taskId, attachmentId).transform()

        fileService.deleteFileByName(taskFilePath, attachment.id.toString())
        taskDataSource.deleteTaskAttachmentById(attachmentId, taskId)
    }

    override suspend fun addScheduledTasks(
        userId: UUID,
        taskId: UUID,
        requestScheduledTask: RequestScheduledTask,
    ): Flow<ScheduledTask> {
        validateTaskExistence(userId, taskId)

        val scheduledTasks =
            createListOfDays(
                requestScheduledTask.fromDate,
                requestScheduledTask.toDate,
                requestScheduledTask.frequency,
            ).map {
                ScheduledTask(
                    taskId,
                    it,
                    requestScheduledTask.time,
                )
            }

        return taskDataSource.addScheduledTask(scheduledTasks)
    }

    private suspend fun validateTaskExistence(
        userId: UUID,
        taskId: UUID,
    ): Task {
        val errorMessageArguments = arrayOf(taskId, userId)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.TASK_NOT_FOUND_EXCEPTION.key,
                *errorMessageArguments,
            )

        return taskDataSource
            .retrieveTaskById(taskId, userId)
            ?: throw RYGException(
                RYGExceptionType.NOT_FOUND,
                errorMessage,
            )
    }

    private suspend fun validateTaskAttachmentExistence(
        userId: UUID,
        taskId: UUID,
        attachmentId: UUID,
    ): TaskAttachment {
        val errorMessageArguments = arrayOf(attachmentId, taskId)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.TASK_ATTACHMENT_NOT_FOUND_EXCEPTION.key,
                *errorMessageArguments,
            )

        validateTaskExistence(userId, taskId)

        return taskDataSource
            .retrieveTaskAttachmentById(attachmentId, taskId)
            ?: throw RYGException(
                RYGExceptionType.NOT_FOUND,
                errorMessage,
            )
    }
}
