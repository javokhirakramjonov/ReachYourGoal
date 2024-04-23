package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.TaskInPlan

class TaskInPlanDto(
    val task: TaskDto,
    val planId: Int,
    val selectedWeekDays: Int,
) : Transformable<TaskInPlan> {
    override fun transform(): TaskInPlan {
        return TaskInPlan(
            taskId = task.id,
            planId = planId,
            selectedWeekDays = selectedWeekDays,
        )
    }
}
