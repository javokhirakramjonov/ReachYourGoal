package me.javahere.reachyourgoal.util

import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.Task
import me.javahere.reachyourgoal.domain.TaskStatus
import me.javahere.reachyourgoal.domain.User
import java.time.Duration

object MockConstants {
    // user
    val existedMockUser =
        User(
            id = "f47ac10b-58cc-4372-a567-0e02b2c3d479".toUUID(),
            firstname = "mock_firstname",
            lastname = "mock_lastname",
            username = "mock_username",
            email = "mock_user@mail.com",
            password = "mock_password",
            role = Role.USER,
        )

    // task
    val existedMockTask =
        Task(
            id = "06e8569a-8c4a-4c3c-98fa-3c42e7c4d12a".toUUID(),
            name = "mock_task",
            description = "mock_description",
            spentTime = Duration.ofHours(2).toMillis(),
            status = TaskStatus.IN_PROGRESS,
            userId = existedMockUser.id!!,
        )
}
