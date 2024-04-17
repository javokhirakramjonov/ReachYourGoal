package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.entity.TaskTag
import me.javahere.reachyourgoal.domain.id.UserId

class RequestCreateTaskTag(
    val name: String,
) {
    fun transform(userId: UserId): TaskTag {
        return TaskTag(
            name = name,
            userId = userId,
        )
    }
}
