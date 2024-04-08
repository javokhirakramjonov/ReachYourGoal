package me.javahere.reachyourgoal

import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.domain.entity.DateFrequency
import me.javahere.reachyourgoal.domain.entity.TaskAttachment
import java.time.LocalDate
import java.time.LocalTime

fun randomString() = (1..10).map { ('a'..'z').random() }.joinToString("")

fun mockUser() =
    RequestRegister(
        username = randomString(),
        email = randomString(),
        password = randomString(),
        lastname = randomString(),
        firstname = randomString(),
    ).transform()

fun mockTask(categoryId: Int) =
    RequestCreateTask(
        name = randomString(),
        description = randomString(),
        categoryId = categoryId,
    ).transform()

fun mockTaskAttachment(taskId: Int) =
    TaskAttachment(
        name = randomString(),
        taskId = taskId,
    )

fun mockTaskSchedule(
    taskId: Int,
    fromDate: LocalDate = LocalDate.now(),
    toDate: LocalDate = LocalDate.now().plusDays(10),
) = RequestCreateTaskSchedule(
    fromDate = fromDate,
    toDate = toDate,
    time = LocalTime.now(),
    frequency = DateFrequency.Days(1),
).transform(taskId)

fun mockRequestRegister() =
    RequestRegister(
        username = randomString(),
        email = randomString(),
        password = randomString(),
        lastname = randomString(),
        firstname = randomString(),
    )
