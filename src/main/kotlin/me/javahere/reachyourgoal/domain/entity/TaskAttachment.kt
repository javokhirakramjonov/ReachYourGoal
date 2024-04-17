package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.domain.id.TaskAttachmentId
import me.javahere.reachyourgoal.domain.id.TaskId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_attachments")
data class TaskAttachment(
    @Id
    @Column("id")
    val id: TaskAttachmentId = TaskAttachmentId(),
    @Column("name")
    val name: String,
    @Column("task_id")
    val taskId: TaskId,
) : Transformable<TaskAttachmentDto> {
    override fun transform(): TaskAttachmentDto {
        return TaskAttachmentDto(
            id = id,
            taskId = taskId,
            fileName = name,
        )
    }
}
