package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskTagId
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "tasks_and_tags")
class TaskAndTag(
    @Column("task_id")
    val taskId: TaskId,
    @Column("tag_id")
    val tagId: TaskTagId,
)
