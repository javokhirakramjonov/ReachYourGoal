package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.entity.Task
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.UserId
import kotlin.time.Duration

class TaskDto(
    val id: TaskId,
    val name: String,
    val description: String?,
    val spentTime: Duration,
) {
    fun transform(userId: UserId): Task {
        return Task(
            id = id.value,
            name = name,
            description = description,
            spentTime = spentTime,
            userId = userId.value,
        )
    }
}
