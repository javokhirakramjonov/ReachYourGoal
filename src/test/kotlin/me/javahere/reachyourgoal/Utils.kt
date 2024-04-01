package me.javahere.reachyourgoal

import me.javahere.reachyourgoal.domain.DateFrequency
import me.javahere.reachyourgoal.domain.TaskAttachment
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

fun randomString() = (1..10).map { ('a'..'z').random() }.joinToString("")

fun mockUser() =
    RequestRegister(
        username = randomString(),
        email = randomString(),
        password = randomString(),
        lastname = randomString(),
        firstname = randomString(),
    ).transform()

fun mockTask(userId: UUID) =
    RequestCreateTask(
        name = randomString(),
        description = randomString(),
    ).transform(userId)

fun mockTaskAttachment(taskId: UUID) =
    TaskAttachment(
        name = randomString(),
        taskId = taskId,
    )

fun mockTaskSchedule(
    taskId: UUID,
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
