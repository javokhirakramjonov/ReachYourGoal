package me.javahere.reachyourgoal.datasource.mock

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.util.MockConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

class MockUserDataSourceTest {

    private val userDataSource = MockUserDataSource()

    private val existedUserId = MockConstants.USER_ID
    private val existedUserUsername = MockConstants.USER_USERNAME
    private val existedUserEmail = MockConstants.USER_EMAIL

    @Test
    fun `should create user`() {
        runBlocking {
            // given
            val user = mockk<User>()
            val userId = UUID.randomUUID()
            every { user.id } returns userId
            ReflectionTestUtils.setField(user, "username", "username")
            every { user.email } returns "email"

            // when
            userDataSource.createUser(user)
            val foundUser = userDataSource.retrieveUserById(userId)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should provide user by userId`() {
        runBlocking {
            // when
            val foundUser = userDataSource.retrieveUserById(existedUserId)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should provide user by username`() {
        runBlocking {
            // when
            val foundUser = userDataSource.retrieveUserByUsername(existedUserUsername)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should provide user by email`() {
        runBlocking {
            // when
            val foundUser = userDataSource.retrieveUserByEmail(existedUserEmail)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should update user`() {
        runBlocking {
            // given
            val user = userDataSource.retrieveUserById(existedUserId)!!
            val updatingUser = user.copy(
                firstName = "updating"
            )

            // when
            userDataSource.updateUser(updatingUser)
            val foundUser = userDataSource.retrieveUserById(existedUserId)!!

            // then
            Assertions.assertEquals(updatingUser, foundUser)
        }
    }
}