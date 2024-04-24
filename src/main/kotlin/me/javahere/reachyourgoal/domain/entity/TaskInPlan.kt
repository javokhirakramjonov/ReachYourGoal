package me.javahere.reachyourgoal.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_in_plan")
data class
TaskInPlan(
    @Id
    @Column("id")
    val id: Int = 0,
    @Column("task_id")
    val taskId: Int,
    @Column("plan_id")
    val planId: Int,
    @Column("selected_week_days")
    val selectedWeekDays: Int,
)
