package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskTagDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskTag
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskTagId
import me.javahere.reachyourgoal.domain.id.UserId

interface TaskTagService {
    suspend fun createTaskTag(
        requestCreateTaskTag: RequestCreateTaskTag,
        userId: UserId,
    ): TaskTagDto

    suspend fun attachTagToTask(
        taskId: TaskId,
        tagId: TaskTagId,
        userId: UserId,
    )

    suspend fun getAllTagsByUserId(userId: UserId): Flow<TaskTagDto>

    suspend fun deleteAllTagsByUserId(userId: UserId)

    suspend fun deleteTagById(
        tagId: TaskTagId,
        userId: UserId,
    )

    suspend fun updateTag(
        taskTagDto: TaskTagDto,
        userId: UserId,
    ): TaskTagDto

    suspend fun detachTagFromTask(
        taskId: TaskId,
        tagId: TaskTagId,
        userId: UserId,
    )
}
