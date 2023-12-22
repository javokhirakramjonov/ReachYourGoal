package me.javahere.reachyourgoal.datasource.mock

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.util.MockConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class MockTaskDataSourceTest {

    private val taskDataSource = MockTaskDataSource()

    private val existedUserId = MockConstants.USER_ID
    private val existedTaskId = MockConstants.TASK_ID

    private val task: Task = mockk()

    init {
        runBlocking {
            val taskId = UUID.randomUUID()
            every { task.id } returns taskId
            every { task.userId } returns existedUserId
        }
    }

    @Test
    fun `should create task`() {
        runBlocking {
            // when
            val createdTask = taskDataSource.createTask(task)

            // then
            val foundTask = taskDataSource.retrieveTaskByTaskIdAndUserId(createdTask.id!!, existedUserId)
            Assertions.assertEquals(task, foundTask)
        }
    }

    @Test
    fun `should provide all task for user`() {
        runBlocking {
            // when
            val tasks = taskDataSource.retrieveAllTasksByUserId(existedUserId)

            // then
            Assertions.assertTrue(tasks.toList().isNotEmpty())
        }
    }

    @Test
    fun `should provide task for taskId and userId`() {
        runBlocking {
            // when
            val foundTask = taskDataSource.retrieveTaskByTaskIdAndUserId(existedTaskId, existedUserId)

            // then
            Assertions.assertNotNull(foundTask)
        }
    }

    @Test
    fun `should update task`() {
        runBlocking {
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
            Assertions.assertEquals(updatingTask, foundTask)
        }
    }

    @Test
    fun `should delete task`() {
        runBlocking {
            // when
            taskDataSource.deleteTaskByTaskIdAndUserId(existedTaskId, existedUserId)

            // then
            val foundTask = taskDataSource.retrieveTaskByTaskIdAndUserId(existedTaskId, existedUserId)

            Assertions.assertNull(foundTask)
        }
    }
}