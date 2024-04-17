package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.domain.entity.TaskAttachment
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.transformCollection
import me.javahere.reachyourgoal.repository.TaskAttachmentRepository
import me.javahere.reachyourgoal.service.FileService
import me.javahere.reachyourgoal.service.TaskAttachmentName
import me.javahere.reachyourgoal.service.TaskAttachmentService
import me.javahere.reachyourgoal.service.TaskService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
class TaskAttachmentServiceImpl(
    private val taskAttachmentRepository: TaskAttachmentRepository,
    @Lazy
    private val taskService: TaskService,
    private val fileService: FileService,
    @Value("\${app.task.attachment.path}") private val taskAttachmentPath: String,
) : TaskAttachmentService {
    @Transactional(rollbackFor = [RYGException::class])
    override suspend fun createTaskAttachment(
        taskId: Int,
        filePart: FilePart,
        userId: Int,
    ): TaskAttachmentDto {
        val taskAttachment =
            TaskAttachment(
                name = filePart.filename(),
                taskId = taskId,
            )

        val createdTaskAttachment = taskAttachmentRepository.save(taskAttachment).transform()

        val newName = createdTaskAttachment.id.toString()

        val isSaved = fileService.createFile(taskAttachmentPath, newName, filePart)

        return if (isSaved) createdTaskAttachment else throw RYGException()
    }

    override suspend fun getTaskAttachmentById(
        attachmentId: Int,
        userId: Int,
    ): Pair<TaskAttachmentName, File> {
        val taskAttachment = validateTaskAttachmentExistence(attachmentId, userId)

        val internalFileName = taskAttachment.id.toString()

        val file =
            fileService.getFileByName(taskAttachmentPath, internalFileName)
                ?: throw RYGException("File(id = $attachmentId) not found")

        return taskAttachment.fileName to file
    }

    override suspend fun getAllTaskAttachmentsByTaskId(
        taskId: Int,
        userId: Int,
    ): Flow<TaskAttachmentDto> {
        taskService.validateTaskExistence(taskId, userId)

        return taskAttachmentRepository
            .findAllByTaskId(taskId)
            .transformCollection()
    }

    @Transactional(rollbackFor = [RYGException::class])
    override suspend fun deleteTaskAttachmentById(
        attachmentId: Int,
        userId: Int,
    ) {
        val taskAttachment = validateTaskAttachmentExistence(attachmentId, userId)

        val internalFileName = taskAttachment.id.toString()

        fileService.deleteFileByName(taskAttachmentPath, internalFileName)

        taskAttachmentRepository.deleteById(attachmentId)
    }

    @Transactional(rollbackFor = [RYGException::class])
    override suspend fun deleteAllTaskAttachmentsByTaskId(
        taskId: Int,
        userId: Int,
    ) {
        taskService.validateTaskExistence(taskId, userId)

        val taskAttachments =
            getAllTaskAttachmentsByTaskId(
                taskId,
                userId,
            )

        taskAttachments.collect {
            val internalFileName = it.id.toString()
            fileService.deleteFileByName(taskAttachmentPath, internalFileName)
        }

        taskAttachmentRepository.deleteAllByTaskId(taskId)
    }

    override suspend fun validateTaskAttachmentExistence(
        attachmentId: Int,
        userId: Int,
    ): TaskAttachmentDto {
        val taskAttachment =
            taskAttachmentRepository
                .findById(attachmentId)
                ?: throw RYGException("Task attachment(id = $attachmentId) not found for user(userId = $userId)")

        taskService.validateTaskExistence(taskAttachment.taskId, userId)

        return taskAttachment.transform()
    }
}
