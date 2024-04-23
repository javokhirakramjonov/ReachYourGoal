package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.TaskAndPlan

class TaskAndPlanDto(
    val task: TaskDto,
    val planId: Int,
    val selectedWeekDays: Int,
) : Transformable<TaskAndPlan> {
    override fun transform(): TaskAndPlan {
        return TaskAndPlan(
            taskId = task.id,
            planId = planId,
            selectedWeekDays = selectedWeekDays,
        )
    }
}
