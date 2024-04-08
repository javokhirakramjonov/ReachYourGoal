package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskTagDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskTag

interface TaskTagService {
    suspend fun createTaskTag(
        requestCreateTaskTag: RequestCreateTaskTag,
        userId: Int,
    ): TaskTagDto

    suspend fun attachTagToTask(
        taskId: Int,
        tagId: Int,
        userId: Int,
    )

    suspend fun getAllTagsByUserId(userId: Int): Flow<TaskTagDto>

    suspend fun deleteAllTagsByUserId(userId: Int)

    suspend fun deleteTagById(
        tagId: Int,
        userId: Int,
    )

    suspend fun updateTag(
        taskTagDto: TaskTagDto,
        userId: Int,
    ): TaskTagDto

    suspend fun detachTagFromTask(
        taskId: Int,
        tagId: Int,
        userId: Int,
    )
}
