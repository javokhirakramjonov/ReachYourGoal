package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.Task

class RequestCreateTask(
    val name: String,
    val description: String? = null,
) {
    fun transform(userId: Int): Task {
        return Task(
            name = name,
            description = description,
            userId = userId,
        )
    }
}
