package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.domain.TaskStatus
import me.javahere.reachyourgoal.mockTask
import me.javahere.reachyourgoal.mockTaskAttachment
import me.javahere.reachyourgoal.mockTaskSchedule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TaskRepositoryTest(
    @Autowired userRepository: UserRepository,
    @Autowired private val taskRepository: TaskRepository,
) : TestWithPredefinedUser(userRepository) {
    @Test
    fun `test add task`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            Assertions.assertNotNull(addedTask.id)
            Assertions.assertEquals(addedTask, task.copy(id = addedTask.id))
        }
    }

    @Test
    fun `test get all tasks`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val foundTask = taskRepository.getAllTasks(predefinedUser.id!!).toList()

            Assertions.assertEquals(listOf(addedTask), foundTask)
        }
    }

    @Test
    fun `test get task by id`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val foundTask = taskRepository.getTaskById(addedTask.id!!, predefinedUser.id!!)

            Assertions.assertEquals(addedTask, foundTask)
        }
    }

    @Test
    fun `test update task`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val updatedTask = taskRepository.updateTask(addedTask.copy(name = "new name"))

            Assertions.assertEquals("new name", updatedTask.name)
        }
    }

    @Test
    fun `test delete task by id`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            taskRepository.deleteTaskById(addedTask.id!!, predefinedUser.id!!)

            val foundTask = taskRepository.getTaskById(addedTask.id!!, predefinedUser.id!!)

            Assertions.assertNull(foundTask)
        }
    }

    @Test
    fun `test get all task attachments`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val foundTaskAttachments = taskRepository.getAllTaskAttachments(addedTask.id!!)

            Assertions.assertEquals(0, foundTaskAttachments.count())
        }
    }

    @Test
    fun `test add task attachment`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskAttachment = mockTaskAttachment(addedTask.id!!)

            val addedTaskAttachment = taskRepository.addTaskAttachment(taskAttachment)

            Assertions.assertNotNull(addedTaskAttachment.id)
            Assertions.assertEquals(addedTaskAttachment, taskAttachment.copy(id = addedTaskAttachment.id))
        }
    }

    @Test
    fun `test get task attachment by id`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskAttachment = mockTaskAttachment(addedTask.id!!)

            val addedTaskAttachment = taskRepository.addTaskAttachment(taskAttachment)

            val foundTaskAttachment = taskRepository.getTaskAttachmentById(addedTaskAttachment.id!!, addedTask.id!!)

            Assertions.assertEquals(addedTaskAttachment, foundTaskAttachment)
        }
    }

    @Test
    fun `test delete task attachment by id`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskAttachment = mockTaskAttachment(addedTask.id!!)

            val addedTaskAttachment = taskRepository.addTaskAttachment(taskAttachment)

            taskRepository.deleteTaskAttachmentById(addedTaskAttachment.id!!, addedTask.id!!)

            val foundTaskAttachment = taskRepository.getTaskAttachmentById(addedTaskAttachment.id!!, addedTask.id!!)

            Assertions.assertNull(foundTaskAttachment)
        }
    }

    @Test
    fun `test add task schedule`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskSchedule = mockTaskSchedule(addedTask.id!!)

            val addedTaskSchedule = taskRepository.addTaskSchedule(taskSchedule).first()

            Assertions.assertNotNull(addedTaskSchedule.scheduleId)
            Assertions.assertEquals(addedTaskSchedule, taskSchedule.first().copy(scheduleId = addedTaskSchedule.scheduleId))
        }
    }

    @Test
    fun `test get task schedule for period`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskSchedules = mockTaskSchedule(addedTask.id!!)

            val addedTaskSchedule = taskRepository.addTaskSchedule(taskSchedules)

            val foundTaskSchedule =
                taskRepository.getTaskScheduleForPeriod(
                    addedTask.id!!,
                    taskSchedules.first().transform().taskDateTime,
                    taskSchedules.last().transform().taskDateTime,
                )

            Assertions.assertEquals(addedTaskSchedule.toList(), foundTaskSchedule.toList())
        }
    }

    @Test
    fun `test get task schedule by id`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskSchedules = mockTaskSchedule(addedTask.id!!)

            val addedTaskSchedule = taskRepository.addTaskSchedule(taskSchedules).first()

            val foundTaskSchedule = taskRepository.getTaskScheduleById(addedTaskSchedule.scheduleId)

            Assertions.assertEquals(addedTaskSchedule, foundTaskSchedule)
        }
    }

    @Test
    fun `test delete task schedule for date times`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskSchedules = mockTaskSchedule(addedTask.id!!)

            val addedTaskSchedules = taskRepository.addTaskSchedule(taskSchedules).toList()

            taskRepository.deleteTaskScheduleForDateTimes(addedTask.id!!, addedTaskSchedules.map { it.taskDateTime })

            addedTaskSchedules.forEach {
                val foundTaskSchedule = taskRepository.getTaskScheduleById(it.scheduleId)
                Assertions.assertNull(foundTaskSchedule)
            }
        }
    }

    @Test
    fun `test update task schedule`() {
        runBlocking {
            val task = mockTask(predefinedUser.id!!)

            val addedTask = taskRepository.addTask(task)

            val taskSchedules = mockTaskSchedule(addedTask.id!!)

            val addedTaskSchedules = taskRepository.addTaskSchedule(taskSchedules).first()

            val updatedTaskSchedule = taskRepository.updateTaskSchedule(addedTaskSchedules.copy(taskStatus = TaskStatus.IN_PROGRESS))

            Assertions.assertEquals(TaskStatus.IN_PROGRESS, updatedTaskSchedule.taskStatus)
        }
    }
}
