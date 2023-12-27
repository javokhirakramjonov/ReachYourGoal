package me.javahere.reachyourgoal.util

import me.javahere.reachyourgoal.domain.Role
import java.util.*

object MockConstants {
    //user
    val USER_ID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
    const val USER_FIRSTNAME = "mock_firstname"
    const val USER_LASTNAME = "mock_lastname"
    const val USER_USERNAME = "mock_username"
    const val USER_EMAIL = "mock_user@mail.com"
    const val USER_PASSWORD = "mock_password"
    val USER_ROLE = Role.USER

    //task
    val TASK_ID = UUID.fromString("06e8569a-8c4a-4c3c-98fa-3c42e7c4d12a")
    const val TASK_NAME = "mock_task"

    //user unconfirmed
    val USER_UNCONFIRMED_ID = UUID.fromString("ea91c764-85f8-4db4-bcc4-3d3b36c7aa42")
    const val USER_UNCONFIRMED_FIRSTNAME = "unconfirmed_mock_firstname"
    const val USER_UNCONFIRMED_LASTNAME = "unconfirmed_mock_lastname"
    const val USER_UNCONFIRMED_USERNAME = "unconfirmed_mock_username"
    const val USER_UNCONFIRMED_EMAIL = "unconfirmed_mock_user@mail.com"
    const val USER_UNCONFIRMED_PASSWORD = "unconfirmed_mock_password"
}