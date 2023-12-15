package me.javahere.reachyourgoal.controller

import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.PostgresExtension
import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.request.RequestLogin
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.repository.UserRepository
import me.javahere.reachyourgoal.security.jwt.JwtService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@ExtendWith(PostgresExtension::class)
class UserControllerTest(
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val jwtService: JwtService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder
) {

    @Test
    @DirtiesContext
    fun shouldLogin() {
        runBlocking {
            val username = "username5"
            val password = "password5"

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
            val refreshToken = responseHeaders["Refresh-Token"]?.get(0)

            val decodedAccessToken = jwtService.decodeAccessToken(accessToken!!)
            jwtService.decodeRefreshToken(refreshToken!!)

            Assertions.assertTrue(jwtService.getRoles(decodedAccessToken).any { it.authority == "USER" })
        }
    }

    @DirtiesContext
    @Test
    fun shouldRegister() {

        val mockUser = RequestRegister(
            "name",
            "name2",
            "username4",
            "email4",
            "pass"
        )

        webTestClient
            .post().uri("/auth/register")
            .bodyValue(mockUser)
            .exchange()
            .expectStatus().isOk
    }

}