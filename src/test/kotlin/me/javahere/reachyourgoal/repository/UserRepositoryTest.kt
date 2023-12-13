package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.TestContainerRelatedTest
import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository
) : TestContainerRelatedTest() {

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