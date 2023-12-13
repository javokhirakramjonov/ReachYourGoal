package me.javahere.reachyourgoal.controller

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.PostgresExtension
import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestLogin
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.repository.TaskRepository
import me.javahere.reachyourgoal.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(PostgresExtension::class)
class TaskControllerTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val taskRepository: TaskRepository
) {

    @Test
    fun shouldAddTask() {
        runBlocking {
            val username = "username2"
            val password = "password"

            val mockUser = User(
                id = null,
                firstName = "first name",
                lastName = "last name",
                username = username,
                email = "dfadsfa",
                password = passwordEncoder.encode(password),
                isAccountLocked = false,
                isEnabled = true,
                isAccountExpired = false,
                isCredentialsExpired = false,
                role = Role.USER
            )

            userRepository.save(mockUser)

            val responseHeaders: HttpHeaders = webTestClient
                .post().uri("/auth/login")
                .bodyValue(RequestLogin(username, password))
                .exchange()
                .expectStatus().isOk
                .expectHeader().exists(HttpHeaders.AUTHORIZATION)
                .expectHeader().exists("Refresh-Token")
                .expectBody().returnResult().responseHeaders

            val accessToken = responseHeaders[HttpHeaders.AUTHORIZATION]?.get(0)

            val requestTaskCreate = RequestTaskCreate(
                "task",
                null
            )

            val response = webTestClient
                .post()
                .uri("/api/v1/tasks")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(requestTaskCreate)
                .exchange()
                .expectStatus().isOk

            val id = response.expectBody(TaskDto::class.java).returnResult().responseBody?.id

            Assertions.assertNotNull(id)

            val createdTask = taskRepository.findAll().firstOrNull { it.id == id }

            Assertions.assertNotNull(createdTask)
        }

    }
}