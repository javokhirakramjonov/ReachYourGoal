package me.javahere.reachyourgoal.domain.entity

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_in_plan")
data class TaskInPlan(
    @Column("task_id")
    val taskId: Int,
    @Column("plan_id")
    val planId: Int,
    @Column("selected_week_days")
    val selectedWeekDays: Int,
)
