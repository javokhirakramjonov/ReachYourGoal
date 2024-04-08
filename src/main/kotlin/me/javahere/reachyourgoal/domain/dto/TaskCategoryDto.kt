package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.TaskCategory

class TaskCategoryDto(
    val id: Int,
    val name: String,
    val parentCategoryId: Int? = null,
) {
    fun transform(userId: Int): TaskCategory {
        return TaskCategory(
            id = id,
            name = name,
            parentCategoryId = parentCategoryId,
            userId = userId,
        )
    }
}
