package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.Task
import java.util.UUID

class RequestCreateTask(
    val name: String,
    val description: String? = null,
) {
    fun transform(userId: UUID): Task {
        return Task(
            name = name,
            description = description,
            userId = userId,
        )
    }
}
