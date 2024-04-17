package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import kotlin.time.Duration

@Table(name = "tasks")
data class Task(
    @Id
    @Column("id")
    val id: Int = 0,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = null,
    @Column("spent_time")
    val spentTime: Duration = Duration.ZERO,
    @Column("user_id")
    val userId: Int,
) : Transformable<TaskDto> {
    override fun transform(): TaskDto {
        return TaskDto(
            id = id,
            name = name,
            description = description,
            spentTime = spentTime,
        )
    }
}
