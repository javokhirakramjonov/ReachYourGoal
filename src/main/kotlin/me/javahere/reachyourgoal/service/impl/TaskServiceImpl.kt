package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.exception.*
import me.javahere.reachyourgoal.localize.MessagesEnum
import me.javahere.reachyourgoal.service.FileService
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.getMessage
import me.javahere.reachyourgoal.util.transformCollection
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class TaskServiceImpl(
    private val taskDataSource: TaskDataSource,
    private val messageSource: ResourceBundleMessageSource,
    private val fileService: FileService,
    @Value("\${app.task-file-path}") private val taskFilePath: String
) : TaskService {

    override suspend fun createTask(task: RequestTaskCreate, userId: UUID): TaskDto {
        return taskDataSource
            .createTask(task.transform(userId))
            .transform()
    }

    override suspend fun getTaskByTaskIdAndUserId(id: UUID, userId: UUID): TaskDto {
        return validateTaskExists(id, userId).transform()
    }

    override fun getAllTasksByUserId(userId: UUID): Flow<TaskDto> {
        return taskDataSource
            .retrieveAllTasksByUserId(userId)
            .transformCollection()
    }

    override suspend fun updateTask(task: TaskDto): TaskDto {
        return taskDataSource
            .updateTask(task.transform())
            .transform()
    }

    override suspend fun deleteTaskByTaskIdAndUserId(taskId: UUID, userId: UUID) {
        taskDataSource.deleteTaskByTaskIdAndUserId(taskId, userId)
    }

    override suspend fun createTaskAttachments(
        userId: UUID,
        taskId: UUID,
        attachments: List<Pair<String, DataBuffer>>
    ): List<Pair<String, TaskAttachmentDto?>> {

        validateTaskExists(userId, taskId)

        return attachments
            .map { (name, content) ->
                val newContent = ByteArray(content.readableByteCount()).also { content.read(it) }

                name to newContent
            }
            .map { (attachmentName, content) ->
                val taskAttachment = TaskAttachment(
                    name = attachmentName,
                    taskId = taskId
                )

                val createdTaskAttachment = taskDataSource.createTaskAttachment(taskAttachment).transform()

                val newName = createdTaskAttachment.taskId.toString()

                val isSaved = fileService.createFile(taskFilePath, newName, content)

                val status = if (isSaved) {
                    createdTaskAttachment
                } else {
                    taskDataSource.deleteTaskAttachmentByAttachmentIdAndTaskId(createdTaskAttachment.id, taskId)

                    null
                }

                attachmentName to status
            }
    }

    override suspend fun getAttachment(userId: UUID, taskId: UUID, attachmentId: UUID): File {
        val attachment = validateAttachmentExists(userId, taskId, attachmentId).transform()

        return fileService.getFileByName(taskFilePath, attachment.id.toString())
    }

    override suspend fun getAllAttachmentsByUserIdAndTaskId(userId: UUID, taskId: UUID): Flow<TaskAttachmentDto> {
        validateTaskExists(userId, taskId)

        return taskDataSource.retrieveAllTaskAttachmentsByTaskId(taskId).transformCollection()
    }

    override suspend fun deleteTaskAttachmentByTaskIdAndAttachmentId(userId: UUID, taskId: UUID, attachmentId: UUID) {
        val attachment = validateAttachmentExists(userId, taskId, attachmentId).transform()

        fileService.deleteFileByName(taskFilePath, attachment.id.toString())
        taskDataSource.deleteTaskAttachmentByAttachmentIdAndTaskId(attachmentId, taskId)
    }

    private suspend fun validateTaskExists(userId: UUID, taskId: UUID): Task {
        val errorMessageArguments = arrayOf(taskId, userId)
        val errorMessage = messageSource.getMessage(
            MessagesEnum.TASK_NOT_FOUND_EXCEPTION.key,
            *errorMessageArguments
        )

        return taskDataSource
            .retrieveTaskByTaskIdAndUserId(taskId, userId)
            ?: throw ExceptionResponse(
                ReachYourGoalException(
                    ReachYourGoalExceptionType.NOT_FOUND,
                    errorMessage
                )
            )
    }

    private suspend fun validateAttachmentExists(userId: UUID, taskId: UUID, attachmentId: UUID): TaskAttachment {
        val errorMessageArguments = arrayOf(attachmentId, taskId)
        val errorMessage = messageSource.getMessage(
            MessagesEnum.TASK_ATTACHMENT_NOT_FOUND_EXCEPTION.key,
            *errorMessageArguments
        )

        validateTaskExists(userId, taskId)

        return taskDataSource
            .retrieveTaskAttachment(attachmentId, taskId)
            ?: throw ExceptionResponse(
                ReachYourGoalException(
                    ReachYourGoalExceptionType.NOT_FOUND,
                    errorMessage
                )
            )
    }
}
