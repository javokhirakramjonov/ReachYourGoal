package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.TaskPlan
import me.javahere.reachyourgoal.domain.id.UserId

class RequestCreateTaskPlan(
    val name: String,
    val description: String? = null,
) {
    fun transform(userId: UserId): TaskPlan {
        return TaskPlan(
            name = name,
            description = description,
            userId = userId.value,
        )
    }
}
