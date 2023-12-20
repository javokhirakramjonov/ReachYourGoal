package me.javahere.reachyourgoal.service

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.datasource.mock.MockTaskDataSource
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskStatus
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.service.impl.TaskServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TaskServiceTest {

    private val mockDataSource = mockk<TaskDataSource>(relaxed = true, relaxUnitFun = true)
    private val mockTaskService = TaskServiceImpl(mockDataSource)

    private val dataSource = MockTaskDataSource()
    private val taskService = TaskServiceImpl(dataSource)

    private val user: User = mockk()
    private val task: Task = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        runBlocking {
            every { user.id } returns UUID.randomUUID()
            every { task.id } returns UUID.randomUUID()
            every { task.userId } returns user.id!!

            dataSource.createTask(task)
        }
    }

    @Test
    fun `should call it's datasource methods`() {
        runBlocking {
            // given
            val task = mockk<RequestTaskCreate>(relaxed = true)
            every { task.name } returns "task"

            // when
            val createdTask = mockTaskService.createTask(task, user.id!!)
            mockTaskService.getTaskByTaskIdAndUserId(createdTask.id, user.id!!)
            mockTaskService.getAllTasksByUserId(user.id!!)
            mockTaskService.updateTask(createdTask)
            mockTaskService.deleteTaskByTaskIdAndUserId(createdTask.id, user.id!!)

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
            val task = spyk(RequestTaskCreate("task", null))

            // when
            val createdTask = taskService.createTask(task, user.id!!)

            // then
            val foundTask = taskService.getTaskByTaskIdAndUserId(createdTask.id, user.id!!)
            Assertions.assertNotNull(foundTask)
        }
    }

    @Test
    fun `should provide all task for user`() {
        runBlocking {
            // when
            val tasks = taskService.getAllTasksByUserId(user.id!!)

            // then
            Assertions.assertTrue(tasks.toList().isNotEmpty())
        }
    }

    @Test
    fun `should provide task for taskId and userId`() {
        runBlocking {
            // when
            val foundTask = taskService.getTaskByTaskIdAndUserId(task.id!!, user.id!!)

            // then
            Assertions.assertEquals(task.transform(), foundTask)
        }
    }

    @Test
    fun `should update task`() {
        runBlocking {
            // given
            val updatingTask = TaskDto(
                id = task.id!!,
                name = "updating task",
                description = null,
                spentTime = 0,
                status = TaskStatus.NOT_STARTED,
                userId = user.id!!
            )

            // when
            val updatedTask = taskService.updateTask(updatingTask)
            val foundTask = taskService.getTaskByTaskIdAndUserId(updatedTask!!.id, user.id!!)

            // then
            Assertions.assertEquals(updatingTask, foundTask)
        }
    }

    @Test
    fun `should delete task`() {
        runBlocking {
            // when
            taskService.deleteTaskByTaskIdAndUserId(task.id!!, user.id!!)

            // then
            val foundTask = taskService.getTaskByTaskIdAndUserId(task.id!!, user.id!!)

            Assertions.assertNull(foundTask)
        }
    }

}