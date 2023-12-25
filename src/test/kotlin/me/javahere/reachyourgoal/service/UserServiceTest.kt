package me.javahere.reachyourgoal.service

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.spyk
import me.javahere.reachyourgoal.datasource.mock.MockUserDataSource
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.service.impl.UserServiceImpl
import me.javahere.reachyourgoal.util.MockConstants
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserServiceTest : StringSpec({

    val dataSource = spyk(MockUserDataSource())
    val userService: UserService = UserServiceImpl(
        userDataSource = dataSource,
        passwordEncoder = BCryptPasswordEncoder()
    )

    val existedUserId = MockConstants.USER_ID
    val existedUserUsername = MockConstants.USER_USERNAME
    val existedUserEmail = MockConstants.USER_EMAIL

    "should call it's datasource methods" {
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
        userService.findUserById(createdUser.id)
        userService.findUserByEmail(createdUser.email)
        userService.findUserByUsername(createdUser.username)
        userService.isUsernameExists(createdUser.username)
        userService.isEmailExists(createdUser.email)
        userService.updateUser(createdUser.id)
        userService.deleteUserById(createdUser.id)
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

    "should check email exists" {
        // when
        val isEmailExists = userService.isEmailExists(existedUserEmail)

        // then
        isEmailExists shouldBe true
    }

    "should check username exists" {
        // when
        val isUsernameExists = userService.isUsernameExists(existedUserUsername)

        // then
        isUsernameExists shouldBe true
    }

    "should find user by email" {
        // when
        val foundUser = userService.findUserByEmail(existedUserEmail)

        // then
        foundUser.shouldNotBeNull()
    }

    "should find user by username" {
        // when
        val foundUser = userService.findUserByUsername(existedUserUsername)

        // then
        foundUser.shouldNotBeNull()
    }

    "should find user by userId" {
        // when
        val foundUser = userService.findUserById(existedUserId)

        // then
        foundUser.shouldNotBeNull()
    }

    "should register user" {
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
        foundUser.shouldNotBeNull()
    }

    "should update user" {
        //given
        val newFirstname = "hello"

        // when
        userService.updateUser(existedUserId, firstName = newFirstname)
        val foundUser = userService.findUserById(existedUserId)

        // then
        newFirstname shouldBe foundUser.firstName
    }

    "should update user's email" {
//        TODO(learn how to implement it)
    }

    "should delete user by userId" {
        // when
        userService.deleteUserById(existedUserId)

        // then
        shouldThrowExactly<ReachYourGoalException> {
            userService.findUserById(existedUserId)
        }
    }

})