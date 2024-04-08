package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.TaskTag

class TaskTagDto(
    val id: Int,
    val name: String,
) {
    fun transform(userId: Int): TaskTag {
        return TaskTag(
            id = id,
            name = name,
            userId = userId,
        )
    }
}
