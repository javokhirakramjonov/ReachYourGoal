package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.TaskPlan

class RequestCreateTaskPlan(
    val name: String,
    val description: String? = null,
) {
    fun transform(userId: Int): TaskPlan {
        return TaskPlan(
            name = name,
            description = description,
            userId = userId,
        )
    }
}
