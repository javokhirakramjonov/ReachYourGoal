package me.javahere.reachyourgoal.domain.entity

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
)

class TaskAndPlanId(
    val taskId: Int,
    val planId: Int,
)
