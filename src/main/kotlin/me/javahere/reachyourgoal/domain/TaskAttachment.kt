package me.javahere.reachyourgoal.domain

import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.util.Transformable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "task_attachments")
class TaskAttachment(
    @Id
    @Column("id")
    val id: UUID? = null,
    @Column("name")
    val name: String,
    @Column("task_id")
    val taskId: UUID,
) : Transformable<TaskAttachmentDto> {
    override fun transform(): TaskAttachmentDto {
        return TaskAttachmentDto(
            id = id!!,
            taskId = taskId,
            fileName = name,
        )
    }
}
