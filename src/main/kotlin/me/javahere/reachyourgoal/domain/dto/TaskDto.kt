package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.Task

class TaskDto(
    val id: Int,
    val name: String,
    val description: String?,
) {
    fun transform(userId: Int): Task {
        return Task(
            id = id,
            name = name,
            description = description,
            userId = userId,
        )
    }
}
