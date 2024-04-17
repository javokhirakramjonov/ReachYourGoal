package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.TaskTag
import me.javahere.reachyourgoal.domain.id.TaskTagId
import me.javahere.reachyourgoal.domain.id.UserId

class TaskTagDto(
    val id: TaskTagId,
    val name: String,
) {
    fun transform(userId: UserId): TaskTag {
        return TaskTag(
            id = id.value,
            name = name,
            userId = userId.value,
        )
    }
}
