package me.javahere.reachyourgoal.service

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.toList
import me.javahere.reachyourgoal.datasource.mock.MockTaskDataSource
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.service.impl.TaskServiceImpl
import me.javahere.reachyourgoal.util.MockConstants
import java.util.*

class TaskServiceTest : StringSpec({
    val dataSource = spyk(MockTaskDataSource())
    val taskService = TaskServiceImpl(dataSource)

    val existedUserId = MockConstants.USER_ID
    val existedTaskId = MockConstants.TASK_ID
    val task = mockk<Task>(relaxed = true)

    beforeAny {
        val taskId = UUID.randomUUID()
        every { task.id } returns taskId
        every { task.userId } returns existedUserId
    }

    "should call it's datasource methods" {
        // given
        val newTask = RequestTaskCreate("task")

        // when
        val createdTask = taskService.createTask(newTask, existedUserId)
        taskService.getTaskByTaskIdAndUserId(createdTask.id, existedUserId)
        taskService.getAllTasksByUserId(existedUserId)
        taskService.updateTask(createdTask)
        taskService.deleteTaskByTaskIdAndUserId(createdTask.id, existedUserId)

        // then
        coVerify(atLeast = 1) { dataSource.createTask(any()) }
        coVerify(atLeast = 1) { dataSource.retrieveTaskByTaskIdAndUserId(any(), any()) }
        coVerify(atLeast = 1) { dataSource.retrieveAllTasksByUserId(any()) }
        coVerify(atLeast = 1) { dataSource.updateTask(any()) }
        coVerify(atLeast = 1) { dataSource.deleteTaskByTaskIdAndUserId(any(), any()) }
    }

    "should create task" {
        // given
        val newTask = RequestTaskCreate("task")

        // when
        val createdTask = taskService.createTask(newTask, existedUserId)

        // then
        val foundTask = taskService.getTaskByTaskIdAndUserId(createdTask.id, existedUserId)
        foundTask.shouldNotBeNull()
    }

    "should provide all task for user" {
        // when
        val tasks = taskService.getAllTasksByUserId(existedUserId)

        // then
        tasks.toList().isNotEmpty() shouldBe true
    }

    "should provide task for taskId and userId" {
        // when
        val foundTask = taskService.getTaskByTaskIdAndUserId(existedTaskId, existedUserId)

        // then
        foundTask.shouldNotBeNull()
    }

    "should update task" {
        // given
        val randomTask = taskService
            .getAllTasksByUserId(existedUserId)
            .toList()
            .random()
        val updatingTask = randomTask.copy(
            name = "updating task"
        )

        // when
        taskService.updateTask(updatingTask)
        val foundTask = taskService.getTaskByTaskIdAndUserId(randomTask.id, existedUserId)

        // then
        updatingTask shouldBe foundTask
    }

    "should delete task" {
        // when
        taskService.deleteTaskByTaskIdAndUserId(existedTaskId, existedUserId)

        // then
        shouldThrowExactly<ReachYourGoalException> {
            taskService.getTaskByTaskIdAndUserId(existedTaskId, existedUserId)
        }
    }

})