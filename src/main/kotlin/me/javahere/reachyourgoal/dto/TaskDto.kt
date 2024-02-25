package me.javahere.reachyourgoal.dto

import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.util.Transformable
import java.util.UUID

class TaskDto(
    val id: UUID,
    val name: String,
    val description: String?,
    val spentTime: Long,
    val userId: UUID,
) : Transformable<Task> {
    override fun transform(): Task {
        return Task(
            id = id,
            name = name,
            description = description,
            spentTime = spentTime,
            userId = userId,
        )
    }
}
