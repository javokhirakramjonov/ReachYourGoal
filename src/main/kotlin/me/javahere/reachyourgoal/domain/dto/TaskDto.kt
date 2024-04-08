package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.Task
import kotlin.time.Duration

class TaskDto(
    val id: Int,
    val name: String,
    val description: String?,
    val spentTime: Duration,
    val categoryId: Int,
) : Transformable<Task> {
    override fun transform(): Task {
        return Task(
            id = id,
            name = name,
            description = description,
            spentTime = spentTime,
            categoryId = categoryId,
        )
    }
}
