package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.UserId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

data class TaskPlan(
    @Id
    @Column("id")
    val id: TaskPlanId = TaskPlanId(),
    @Column("user_id")
    val userId: UserId,
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
