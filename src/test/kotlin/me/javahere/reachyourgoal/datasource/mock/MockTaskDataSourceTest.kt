package me.javahere.reachyourgoal.datasource.mock

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.util.MockConstants
import java.util.*

class MockTaskDataSourceTest : StringSpec({

    val taskDataSource = MockTaskDataSource()

    val existedUserId = MockConstants.USER_ID
    val existedTaskId = MockConstants.TASK_ID

    val task: Task = mockk()

    beforeAny {
        val taskId = UUID.randomUUID()
        every { task.id } returns taskId
        every { task.userId } returns existedUserId
    }

    "should create task" {
        // when
        val createdTask = taskDataSource.createTask(task)

        // then
        val foundTask = taskDataSource.retrieveTaskByTaskIdAndUserId(createdTask.id!!, existedUserId)
        task shouldBe foundTask
    }

    "should provide all task for user" {
        // when
        val tasks = taskDataSource.retrieveAllTasksByUserId(existedUserId)

        // then
        tasks.toList().isNotEmpty() shouldBe true
    }

    "should provide task for taskId and userId" {
        // when
        val foundTask = taskDataSource.retrieveTaskByTaskIdAndUserId(existedTaskId, existedUserId)

        // then
        foundTask.shouldNotBeNull()
    }

    "should update task" {
        // given
        val randomTask = taskDataSource
            .retrieveAllTasksByUserId(existedUserId)
            .toList()
            .random()
        val updatingTask = randomTask.copy(
            name = "updating"
        )

        // when
        taskDataSource.updateTask(updatingTask)
        val foundTask = taskDataSource.retrieveTaskByTaskIdAndUserId(updatingTask.id!!, existedUserId)

        // then
        updatingTask shouldBe foundTask
    }

    "should delete task" {
        // when
        taskDataSource.deleteTaskByTaskIdAndUserId(existedTaskId, existedUserId)

        // then
        val foundTask = taskDataSource.retrieveTaskByTaskIdAndUserId(existedTaskId, existedUserId)

        foundTask.shouldBeNull()
    }
})