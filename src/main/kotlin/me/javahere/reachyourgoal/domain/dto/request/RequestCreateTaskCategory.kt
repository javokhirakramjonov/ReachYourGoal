package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.TaskCategory

class RequestCreateTaskCategory(
    val name: String,
    val parentCategoryId: Int? = null,
) {
    fun transform(userId: Int): TaskCategory {
        return TaskCategory(
            name = name,
            parentCategoryId = parentCategoryId,
            userId = userId,
        )
    }
}
