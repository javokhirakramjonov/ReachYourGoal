package me.javahere.reachyourgoal.security

import me.javahere.reachyourgoal.TestContainerRelatedTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import java.time.Duration
import java.util.*

@AutoConfigureWebTestClient
class JwtServiceTest(
    @Autowired private val jwtService: JwtService
) : TestContainerRelatedTest() {

    private val EXPIRE_ACCESS_TOKEN = Duration.ofMinutes(15).toMillis().toInt()

    @Test
    fun testTokenGeneration() {
        // Arrange
        val username = "testUser"

        // Act
        val token = jwtService.generate(
            username,
            EXPIRE_ACCESS_TOKEN,
            arrayOf("USER"),
            "secret"
        )

        // Assert
        // Add assertions for the generated token, e.g., validate claims
        Assertions.assertEquals(username, jwtService.getUsername(token))
    }

    @Test
    fun testTokenValidation() {
        // Arrange
        val username = "testUser"
        val validToken = jwtService.generate(
            username,
            EXPIRE_ACCESS_TOKEN,
            arrayOf("USER"),
            "secret"
        )

        // Act
        val decodedJWT = jwtService.decodeAccessToken(validToken)
        val isValid = decodedJWT.expiresAt.after(Date())

        // Assert
        Assertions.assertTrue(isValid)
    }
}
