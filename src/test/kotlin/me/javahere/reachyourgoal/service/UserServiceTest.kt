package me.javahere.reachyourgoal.service

import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.datasource.mock.MockUserDataSource
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.service.impl.UserServiceImpl
import me.javahere.reachyourgoal.util.MockConstants
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserServiceTest {

    private val dataSource = spyk(MockUserDataSource())
    private val userService: UserService = UserServiceImpl(
        userDataSource = dataSource,
        passwordEncoder = BCryptPasswordEncoder()
    )

    private val existedUserId = MockConstants.USER_ID
    private val existedUserUsername = MockConstants.USER_USERNAME
    private val existedUserEmail = MockConstants.USER_EMAIL

    @Test
    fun `should call it's datasource methods`() {
        runBlocking {
            // given
            val user = RequestRegister(
                firstname = "firstname",
                lastname = "lastname",
                username = "username",
                email = "email",
                password = "password"
            )

            // when
            userService.registerUser(user)
            userService.findUserById(existedUserId)
            userService.findUserByEmail(user.email)
            userService.findUserByUsername(user.username)
            userService.isUsernameExists(user.username)
            userService.isEmailExists(user.email)
            userService.updateUser(existedUserId)
            userService.deleteUserById(existedUserId)
//            TODO(uncomment when service implements)
//            userService.updateEmail(existedUserId, "newEmail")

            // then
            coVerify(atLeast = 1) { dataSource.createUser(any()) }
            coVerify(atLeast = 1) { dataSource.retrieveUserById(any()) }
            coVerify(atLeast = 1) { dataSource.retrieveUserByEmail(any()) }
            coVerify(atLeast = 1) { dataSource.retrieveUserByUsername(any()) }
            coVerify(atLeast = 1) { dataSource.updateUser(any()) }
            coVerify(atLeast = 1) { dataSource.deleteUserById(any()) }
        }
    }

    @Test
    fun `should check email exists`() {
        runBlocking {
            // when
            val isEmailExists = userService.isEmailExists(existedUserEmail)

            // then
            Assertions.assertTrue(isEmailExists)
        }
    }

    @Test
    fun `should check username exists`() {
        runBlocking {
            // when
            val isUsernameExists = userService.isUsernameExists(existedUserUsername)

            // then
            Assertions.assertTrue(isUsernameExists)
        }
    }

    @Test
    fun `should find user by email`() {
        runBlocking {
            // when
            val foundUser = userService.findUserByEmail(existedUserEmail)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should find user by username`() {
        runBlocking {
            // when
            val foundUser = userService.findUserByUsername(existedUserUsername)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should find user by userId`() {
        runBlocking {
            // when
            val foundUser = userService.findUserById(existedUserId)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should register user`() {
        runBlocking {
            // given
            val user = RequestRegister(
                firstname = "firstname",
                lastname = "lastname",
                username = "username",
                email = "email",
                password = "password"
            )

            // when
            val createdUser = userService.registerUser(user)
            val foundUser = userService.findUserById(createdUser.id)

            // then
            Assertions.assertNotNull(foundUser)
        }
    }

    @Test
    fun `should update user`() {
        runBlocking {
            //given
            val newFirstname = "hello"

            // when
            userService.updateUser(existedUserId, firstName = newFirstname)
            val foundUser = userService.findUserById(existedUserId)

            // then
            Assertions.assertEquals(newFirstname, foundUser?.firstName)
        }
    }

    @Test
    fun `should update user's email`() {
//        TODO(learn how to implement it)
    }

    @Test
    fun `should delete user by userId`() {
        runBlocking {
            // when
            userService.deleteUserById(existedUserId)
            val foundUser = userService.findUserById(existedUserId)

            // then
            Assertions.assertNull(foundUser)
        }
    }

}