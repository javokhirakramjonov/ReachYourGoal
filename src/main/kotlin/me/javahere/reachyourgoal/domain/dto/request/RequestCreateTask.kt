package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.Task
import me.javahere.reachyourgoal.domain.id.UserId

class RequestCreateTask(
    val name: String,
    val description: String? = null,
) {
    fun transform(userId: UserId): Task {
        return Task(
            name = name,
            description = description,
            userId = userId,
        )
    }
}
