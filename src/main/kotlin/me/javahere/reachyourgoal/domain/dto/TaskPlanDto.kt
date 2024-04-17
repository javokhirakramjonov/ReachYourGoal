package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.TaskPlan

class TaskPlanDto(
    val id: Int,
    val name: String,
    val description: String? = null,
) {
    fun transform(userId: Int): TaskPlan {
        return TaskPlan(
            id = id,
            userId = userId,
            name = name,
            description = description,
        )
    }
}
