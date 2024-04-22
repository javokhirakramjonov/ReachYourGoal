package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.TaskPlan
import java.time.LocalDate

class TaskPlanDto(
    val id: Int,
    val name: String,
    val description: String? = null,
    val fromDate: LocalDate? = null,
    val toDate: LocalDate? = null,
) {
    fun transform(userId: Int): TaskPlan {
        return TaskPlan(
            id = id,
            userId = userId,
            name = name,
            description = description,
            fromDate = fromDate,
            toDate = toDate,
        )
    }
}
