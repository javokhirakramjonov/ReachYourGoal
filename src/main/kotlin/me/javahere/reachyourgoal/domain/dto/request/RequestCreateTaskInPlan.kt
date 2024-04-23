package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.TaskInPlan

class RequestCreateTaskInPlan(
    val taskId: Int,
    val planId: Int,
    val selectedWeekDays: Int,
) : Transformable<TaskInPlan> {
    override fun transform(): TaskInPlan {
        return TaskInPlan(
            taskId = taskId,
            planId = planId,
            selectedWeekDays = selectedWeekDays,
        )
    }
}
