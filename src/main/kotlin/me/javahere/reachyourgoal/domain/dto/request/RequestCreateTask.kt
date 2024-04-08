package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.Task

class RequestCreateTask(
    val name: String,
    val description: String? = null,
    val categoryId: Int,
) {
    fun transform(): Task {
        return Task(
            name = name,
            description = description,
            categoryId = categoryId,
        )
    }
}
