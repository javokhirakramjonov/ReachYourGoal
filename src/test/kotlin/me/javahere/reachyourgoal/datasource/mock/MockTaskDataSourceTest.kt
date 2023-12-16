package me.javahere.reachyourgoal.datasource.mock

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class MockTaskDataSourceTest {

    private val mockTaskDataSource = MockTaskDataSource()
    private val mockUser: User = mockk<User>()
    private val mockTask: Task = mockk()

    @BeforeEach
    fun setup() {
        runBlocking {
            every { mockUser.id } returns UUID.randomUUID()
            every { mockTask.id } returns UUID.randomUUID()
            every { mockTask.userId } returns mockUser.id!!

            mockTaskDataSource.createTask(mockTask)
        }
    }

    @Test
    fun `should create task`() {
        runBlocking {
            // given
            val task: Task = mockk()
            every { task.id } returns UUID.randomUUID()
            every { task.userId } returns mockUser.id!!

            // when
            mockTaskDataSource.createTask(task)

            // then
            val foundTask = mockTaskDataSource.retrieveTaskByTaskIdAndUserId(task.id!!, mockUser.id!!)
            Assertions.assertEquals(task, foundTask)
        }
    }

    @Test
    fun `should provide all task for user`() {
        runBlocking {
            // when
            val tasks = mockTaskDataSource.retrieveAllTasksByUserId(mockUser.id!!)

            // then
            Assertions.assertTrue(tasks.toList().isNotEmpty())
        }
    }

    @Test
    fun `should provide task for taskId and userId`() {
        runBlocking {
            // when
            val foundTask = mockTaskDataSource.retrieveTaskByTaskIdAndUserId(mockTask.id!!, mockUser.id!!)

            // then
            Assertions.assertEquals(mockTask, foundTask)
        }
    }

    @Test
    fun `should update task`() {
        runBlocking {
            // given
            val updatedTask = mockk<Task>()
            every { updatedTask.id } returns mockTask.id!!
            every { updatedTask.userId } returns mockUser.id!!
            every { updatedTask.name } returns "new task"

            // when
            mockTaskDataSource.updateTask(updatedTask)
            val foundTask = mockTaskDataSource.retrieveTaskByTaskIdAndUserId(updatedTask.id!!, mockUser.id!!)

            // then
            Assertions.assertEquals(updatedTask, foundTask)
        }
    }

    @Test
    fun `should delete task`() {
        runBlocking {
            // when
            mockTaskDataSource.deleteTaskByTaskIdAndUserId(mockTask.id!!, mockUser.id!!)

            // then
            val foundTask = mockTaskDataSource.retrieveTaskByTaskIdAndUserId(mockTask.id!!, mockUser.id!!)

            Assertions.assertNull(foundTask)
        }
    }
}