package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.PostgresExtension
import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(PostgresExtension::class)
class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository
) {

    @Test
    fun shouldAddUser() {
        runBlocking {
            val mockUser = User(
                id = null,
                firstName = "first name",
                lastName = "last name",
                username = "user name",
                email = "email",
                password = "password",
                isAccountLocked = false,
                isEnabled = true,
                isAccountExpired = false,
                isCredentialsExpired = false,
                role = Role.USER
            )

            val savedUser = userRepository.save(mockUser)

            Assertions.assertNotNull(userRepository.findById(savedUser.id!!))
            Assertions.assertEquals(1, userRepository.findAll().count())
        }
    }

}