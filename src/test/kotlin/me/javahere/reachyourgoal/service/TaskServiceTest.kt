package me.javahere.reachyourgoal.service

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.datasource.mock.MockTaskDataSource
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.service.impl.TaskServiceImpl
import me.javahere.reachyourgoal.util.MockConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class TaskServiceTest {

    private val mockDataSource = mockk<TaskDataSource>(relaxed = true, relaxUnitFun = true)
    private val mockTaskService = TaskServiceImpl(mockDataSource)

    private val dataSource = MockTaskDataSource()
    private val taskService = TaskServiceImpl(dataSource)

    private val existedUserId = MockConstants.USER_ID
    private val existedTaskId = MockConstants.TASK_ID
    private val task = mockk<Task>(relaxed = true)

    init {
        runBlocking {
            val taskId = UUID.randomUUID()
            every { task.id } returns taskId
            every { task.userId } returns existedUserId
        }
    }

    @Test
    fun `should call it's datasource methods`() {
        runBlocking {
            // given
            val task = mockk<RequestTaskCreate>(relaxed = true)
            every { task.name } returns "task"

            // when
            val createdTask = mockTaskService.createTask(task, existedUserId)
            mockTaskService.getTaskByTaskIdAndUserId(createdTask.id, existedUserId)
            mockTaskService.getAllTasksByUserId(existedUserId)
            mockTaskService.updateTask(createdTask)
            mockTaskService.deleteTaskByTaskIdAndUserId(createdTask.id, existedUserId)

            // then
            coVerify(atLeast = 1) { mockDataSource.createTask(any()) }
            coVerify(atLeast = 1) { mockDataSource.retrieveTaskByTaskIdAndUserId(any(), any()) }
            coVerify(atLeast = 1) { mockDataSource.retrieveAllTasksByUserId(any()) }
            coVerify(atLeast = 1) { mockDataSource.updateTask(any()) }
            coVerify(atLeast = 1) { mockDataSource.deleteTaskByTaskIdAndUserId(any(), any()) }
        }
    }

    @Test
    fun `should create task`() {
        runBlocking {
            // given
            val task = RequestTaskCreate("task")

            // when
            val createdTask = taskService.createTask(task, existedUserId)

            // then
            val foundTask = taskService.getTaskByTaskIdAndUserId(createdTask.id, existedUserId)
            Assertions.assertNotNull(foundTask)
        }
    }

    @Test
    fun `should provide all task for user`() {
        runBlocking {
            // when
            val tasks = taskService.getAllTasksByUserId(existedUserId)

            // then
            Assertions.assertTrue(tasks.toList().isNotEmpty())
        }
    }

    @Test
    fun `should provide task for taskId and userId`() {
        runBlocking {
            // when
            val foundTask = taskService.getTaskByTaskIdAndUserId(existedTaskId, existedUserId)

            // then
            Assertions.assertNotNull(foundTask)
        }
    }

    @Test
    fun `should update task`() {
        runBlocking {
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
            Assertions.assertEquals(updatingTask, foundTask)
        }
    }

    @Test
    fun `should delete task`() {
        runBlocking {
            // when
            taskService.deleteTaskByTaskIdAndUserId(existedTaskId, existedUserId)

            // then
            val foundTask = taskService.getTaskByTaskIdAndUserId(existedTaskId, existedUserId)

            Assertions.assertNull(foundTask)
        }
    }

}