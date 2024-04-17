package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskTagDto
import me.javahere.reachyourgoal.domain.id.TaskTagId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_tags")
data class TaskTag(
    @Id
    @Column("id")
    val id: Int = 0,
    @Column("name")
    val name: String,
    @Column("user_id")
    val userId: Int,
) : Transformable<TaskTagDto> {
    override fun transform(): TaskTagDto {
        return TaskTagDto(
            id = TaskTagId(id),
            name = name,
        )
    }
}
