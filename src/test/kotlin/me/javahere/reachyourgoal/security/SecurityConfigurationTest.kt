package me.javahere.reachyourgoal.security

import me.javahere.reachyourgoal.PostgresExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@AutoConfigureWebTestClient
@SpringBootTest
@ExtendWith(PostgresExtension::class)
class SecurityConfigurationTest(
    @Autowired private val webTestClient: WebTestClient
) {

    @Test
    fun `test unauthenticated access to protected endpoints`() {
        webTestClient.get()
            .uri("/api/someEndpoint")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    @WithMockUser(authorities = ["USER"])
    fun `test authenticated access to protected endpoints`() {
        webTestClient.get()
            .uri("/api/someEndpoint")
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    @WithMockUser(authorities = ["ADMIN"])
    fun `test admin access to admin endpoint`() {
        webTestClient.get()
            .uri("/admin/someEndpoint")
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    @WithMockUser
    fun `test unauthorized access to admin endpoint`() {
        webTestClient.get()
            .uri("/admin/someEndpoint")
            .exchange()
            .expectStatus().isForbidden
    }
}
