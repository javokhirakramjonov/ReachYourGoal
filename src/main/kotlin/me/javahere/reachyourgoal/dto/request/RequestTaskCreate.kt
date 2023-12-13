package me.javahere.reachyourgoal.dto.request

import me.javahere.reachyourgoal.domain.Task
import java.util.*

data class RequestTaskCreate(
    val name: String,
    val description: String?
) {

    fun transform(userId: UUID): Task {
        return Task(
            id = null,
            name = name,
            description = description,
            userId = userId
        )
    }

}