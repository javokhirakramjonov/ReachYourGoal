package me.javahere.reachyourgoal.domain

import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.util.Transformable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "tasks")
class Task(
    @Id
    @Column("id")
    val id: UUID? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = null,
    @Column("spent_time")
    val spentTime: Long = 0L,
    @Column("status")
    val status: TaskStatus = TaskStatus.NOT_STARTED,
    @Column("user_id")
    val userId: UUID,
) : Transformable<TaskDto> {
    override fun transform(): TaskDto {
        return TaskDto(
            id = id!!,
            name = name,
            description = description,
            spentTime = spentTime,
            status = status,
            userId = userId,
        )
    }
}
