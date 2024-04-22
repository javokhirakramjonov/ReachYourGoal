package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskTagDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskTag
import me.javahere.reachyourgoal.domain.entity.TaskAndTag
import me.javahere.reachyourgoal.domain.entity.TaskAndTagId
import me.javahere.reachyourgoal.domain.entity.TaskTag
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.transformCollection
import me.javahere.reachyourgoal.repository.TaskAndTagRepository
import me.javahere.reachyourgoal.repository.TaskTagRepository
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.service.TaskTagService
import org.springframework.stereotype.Service

@Service
class TaskTagServiceImpl(
    private val taskAndTagRepository: TaskAndTagRepository,
    private val taskTagRepository: TaskTagRepository,
    private val taskService: TaskService,
) : TaskTagService {
    private suspend fun validateTaskTagExistence(
        tagId: Int,
        userId: Int,
    ): TaskTag {
        return taskTagRepository
            .findByIdAndUserId(
                tagId,
                userId,
            )
            ?: throw RYGException("Task tag(id = $tagId) not found for user(userId = $userId)")
    }

    private suspend fun validateTaskAndTagConnected(taskAndTagId: TaskAndTagId): TaskAndTag {
        return taskAndTagRepository
            .findById(taskAndTagId)
            ?: throw RYGException("Task and tag connection(id = $taskAndTagId) not found")
    }

    override suspend fun createTaskTag(
        requestCreateTaskTag: RequestCreateTaskTag,
        userId: Int,
    ): TaskTagDto {
        val taskTag = requestCreateTaskTag.transform(userId)

        return taskTagRepository.save(taskTag).transform()
    }

    override suspend fun attachTagToTask(
        taskId: Int,
        tagId: Int,
        userId: Int,
    ) {
        validateTaskTagExistence(tagId, userId)
        taskService.validateTaskExistence(taskId, userId)

        val taskAndTag = TaskAndTag(taskId, tagId)

        taskAndTagRepository.save(taskAndTag)
    }

    override suspend fun getAllTagsByUserId(userId: Int): Flow<TaskTagDto> {
        return taskTagRepository
            .findAllByUserId(userId)
            .transformCollection()
    }

    override suspend fun deleteAllTagsByUserId(userId: Int) {
        taskTagRepository.deleteAllByUserId(userId)
    }

    override suspend fun deleteTagById(
        tagId: Int,
        userId: Int,
    ) {
        validateTaskTagExistence(tagId, userId)

        taskTagRepository.deleteByIdAndUserId(tagId, userId)
    }

    override suspend fun updateTag(
        taskTagDto: TaskTagDto,
        userId: Int,
    ): TaskTagDto {
        validateTaskTagExistence(taskTagDto.id, userId)

        val taskTag = taskTagDto.transform(userId)

        return taskTagRepository.save(taskTag).transform()
    }

    override suspend fun detachTagFromTask(
        taskId: Int,
        tagId: Int,
        userId: Int,
    ) {
        validateTaskTagExistence(tagId, userId)
        taskService.validateTaskExistence(taskId, userId)

        val taskAndTagId = TaskAndTagId(taskId, tagId)
        validateTaskAndTagConnected(taskAndTagId)

        taskAndTagRepository.deleteById(taskAndTagId)
    }
}
