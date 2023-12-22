package me.javahere.reachyourgoal.util

import me.javahere.reachyourgoal.domain.Role
import java.util.*

object MockConstants {
    val USER_ID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
    const val USER_FIRSTNAME = "mock_firstname"
    const val USER_LASTNAME = "mock_lastname"
    const val USER_USERNAME = "mock_username"
    const val USER_EMAIL = "mock_user@mail.com"
    const val USER_PASSWORD = "mock_password"
    val USER_ROLE = Role.USER

    val TASK_ID = UUID.fromString("06e8569a-8c4a-4c3c-98fa-3c42e7c4d12a")
    const val TASK_NAME = "mock_task"
}