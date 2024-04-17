package me.javahere.reachyourgoal.domain.entity

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "tasks_and_tags")
class TaskAndTag(
    @Column("task_id")
    val taskId: Int,
    @Column("tag_id")
    val tagId: Int,
)
