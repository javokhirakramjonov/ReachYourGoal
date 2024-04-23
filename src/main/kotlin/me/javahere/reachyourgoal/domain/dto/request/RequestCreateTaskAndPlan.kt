package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.TaskAndPlan

class RequestCreateTaskAndPlan(
    val taskId: Int,
    val planId: Int,
    val selectedWeekDays: Int,
) : Transformable<TaskAndPlan> {
    override fun transform(): TaskAndPlan {
        return TaskAndPlan(
            taskId = taskId,
            planId = planId,
            selectedWeekDays = selectedWeekDays,
        )
    }
}
