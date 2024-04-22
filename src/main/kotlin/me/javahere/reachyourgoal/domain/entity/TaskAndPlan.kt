package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskAndPlanDto
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_and_plan")
data class TaskAndPlan(
    @Column("task_id")
    val taskId: Int,
    @Column("plan_id")
    val planId: Int,
    @Column("selected_week_days")
    val selectedWeekDays: Int,
) : Transformable<TaskAndPlanDto> {
    override fun transform(): TaskAndPlanDto {
        return TaskAndPlanDto(
            taskId = taskId,
            planId = planId,
            selectedWeekDays = selectedWeekDays,
        )
    }
}

class TaskAndPlanId(
    val taskId: Int,
    val planId: Int,
)
