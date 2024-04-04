package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_attachments")
data class TaskAttachment(
    @Id
    @Column("id")
    val id: Int = 0,
    @Column("name")
    val name: String,
    @Column("task_id")
    val taskId: Int,
) : Transformable<TaskAttachmentDto> {
    override fun transform(): TaskAttachmentDto {
        return TaskAttachmentDto(
            id = id,
            taskId = taskId,
            fileName = name,
        )
    }
}
