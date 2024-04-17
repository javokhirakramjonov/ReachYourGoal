package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "task_schedules")
data class TaskSchedule(
    @Id
    @Column("id")
    val scheduleId: Int = 0,
    @Column("task_id")
    val taskId: Int,
    @Column("task_plan_id")
    val taskPlanId: TaskPlanId,
    @Column("task_date_time")
    val taskDate: LocalDate,
    @Column("task_status")
    val taskStatus: TaskStatus = TaskStatus.NOT_STARTED,
) : Transformable<TaskScheduleDto> {
    override fun transform(): TaskScheduleDto {
        return TaskScheduleDto(
            scheduleId = TaskScheduleId(scheduleId),
            taskId = TaskId(taskId),
            taskPlanId = taskPlanId,
            taskDate = taskDate,
            taskStatus = taskStatus,
        )
    }
}
