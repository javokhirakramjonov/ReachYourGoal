package me.javahere.reachyourgoal.service

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.service.impl.TaskServiceImpl
import org.junit.jupiter.api.Test
import java.util.*

class TaskServiceTest {

    private val dataSource = mockk<TaskDataSource>(relaxed = true, relaxUnitFun = true)
    private val taskService = TaskServiceImpl(dataSource)

    @Test
    fun `should call it's datasource methods`() {
        runBlocking {
            // given
            val task = mockk<RequestTaskCreate>(relaxed = true)
            val user = mockk<User>()

            every { user.id } returns UUID.randomUUID()
            every { task.name } returns "task"

            // when
            val createdTask = taskService.createTask(task, user.id!!)
            taskService.getTaskByTaskIdAndUserId(createdTask.id, user.id!!)
            taskService.getAllTasksByUserId(user.id!!)
            taskService.updateTask(createdTask)
            taskService.deleteTaskByTaskIdAndUserId(createdTask.id, user.id!!)

            // then
            coVerify(atLeast = 1) { dataSource.createTask(any()) }
            coVerify(atLeast = 1) { dataSource.retrieveTaskByTaskIdAndUserId(any(), any()) }
            coVerify(atLeast = 1) { dataSource.retrieveAllTasksByUserId(any()) }
            coVerify(atLeast = 1) { dataSource.updateTask(any()) }
            coVerify(atLeast = 1) { dataSource.deleteTaskByTaskIdAndUserId(any(), any()) }
        }
    }

}