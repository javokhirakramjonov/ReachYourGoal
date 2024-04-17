package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.TaskPlan
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.UserId

class TaskPlanDto(
    val id: TaskPlanId,
    val name: String,
    val description: String? = null,
) {
    fun transform(userId: UserId): TaskPlan {
        return TaskPlan(
            id = id,
            userId = userId,
            name = name,
            description = description,
        )
    }
}
