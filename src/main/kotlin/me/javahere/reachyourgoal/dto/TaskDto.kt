package me.javahere.reachyourgoal.dto

import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskStatus
import me.javahere.reachyourgoal.util.Transformable
import java.util.*

data class TaskDto(
    val id: UUID,
    val name: String,
    val description: String?,
    val spentTime: Long,
    val status: TaskStatus,
    val userId: UUID,
) : Transformable<Task> {
    override fun transform(): Task {
        return Task(
            id = id,
            name = name,
            description = description,
            spentTime = spentTime,
            status = status,
            userId = userId,
        )
    }
}
