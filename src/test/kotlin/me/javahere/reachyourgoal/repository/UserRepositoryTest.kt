package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.PostgresExtension
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
@ExtendWith(PostgresExtension::class)
class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository,
) {
    private fun randomString() = (1..10).map { ('a'..'z').random() }.joinToString("")

    private fun mockUser() =
        RequestRegister(
            username = randomString(),
            email = randomString(),
            password = randomString(),
            lastname = randomString(),
            firstname = randomString(),
        ).transform()

    @Test
    fun `add user`() =
        runBlocking {
            val user = mockUser()

            val addedUser = userRepository.addUser(user)

            Assertions.assertNotNull(addedUser.id)
            Assertions.assertEquals(addedUser, user.copy(id = addedUser.id))
        }

    @Test
    fun `get user by id`() =
        runBlocking {
            val user = mockUser()

            val addedUser = userRepository.addUser(user)

            val foundUser = userRepository.getUserById(addedUser.id!!)

            Assertions.assertEquals(addedUser, foundUser)
        }

    @Test
    fun `get user by username`() =
        runBlocking {
            val user = mockUser()

            val addedUser = userRepository.addUser(user)

            val foundUser = userRepository.getUserByUsername(addedUser.username)

            Assertions.assertEquals(addedUser, foundUser)
        }

    @Test
    fun `get user by email`() =
        runBlocking {
            val user = mockUser()

            val addedUser = userRepository.addUser(user)

            val foundUser = userRepository.getUserByEmail(addedUser.email)

            Assertions.assertEquals(addedUser, foundUser)
        }

    @Test
    fun `update user`() =
        runBlocking {
            val user = mockUser()

            val addedUser = userRepository.addUser(user)

            val updatedUser = userRepository.updateUser(addedUser.copy(username = "newUsername"))

            Assertions.assertEquals(addedUser.copy(username = "newUsername"), updatedUser)
        }

    @Test
    fun `delete user by id`() =
        runBlocking {
            val user = mockUser()

            val addedUser = userRepository.addUser(user)

            userRepository.deleteUserById(addedUser.id!!)

            val foundUser = userRepository.getUserById(addedUser.id!!)

            Assertions.assertNull(foundUser)
        }

    @Test
    fun `delete unconfirmed users before`() =
        runBlocking {
            val user = mockUser()

            userRepository.addUser(user)

            userRepository.deleteUnconfirmedUsersBefore(LocalDateTime.now())

            val foundUser = userRepository.getUserByEmail(user.email)

            Assertions.assertNull(foundUser)
        }
}
