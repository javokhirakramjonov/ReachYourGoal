package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_plans")
data class TaskPlan(
    @Id
    @Column("id")
    val id: Int = 0,
    @Column("user_id")
    val userId: Int,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = null,
) : Transformable<TaskPlanDto> {
    override fun transform(): TaskPlanDto {
        return TaskPlanDto(
            id = id,
            name = name,
            description = description,
        )
    }
}
