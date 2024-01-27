package me.javahere.reachyourgoal.dto.request

import me.javahere.reachyourgoal.domain.Task
import java.util.*

data class RequestTaskCreate(
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
