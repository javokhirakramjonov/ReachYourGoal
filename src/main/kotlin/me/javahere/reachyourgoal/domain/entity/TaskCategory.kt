package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskCategoryDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "task_categories")
data class TaskCategory(
    @Id
    @Column("id")
    val id: Int = 0,
    @Column("name")
    val name: String,
    @Column("user_id")
    val userId: Int,
    @Column("parent_category_id")
    val parentCategoryId: Int? = null,
) : Transformable<TaskCategoryDto> {
    override fun transform(): TaskCategoryDto {
        return TaskCategoryDto(
            id = id,
            name = name,
            parentCategoryId = parentCategoryId,
        )
    }
}
