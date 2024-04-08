package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.TaskTag

class RequestCreateTaskTag(
    val name: String,
) {
    fun transform(userId: Int): TaskTag {
        return TaskTag(
            name = name,
            userId = userId,
        )
    }
}
