package me.javahere.reachyourgoal.datasource.mock

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.util.MockConstants
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

class MockUserDataSourceTest : StringSpec({

    val userDataSource = MockUserDataSource()

    val existedUserId = MockConstants.USER_ID
    val existedUserUsername = MockConstants.USER_USERNAME
    val existedUserEmail = MockConstants.USER_EMAIL

    "should create user" {
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
        foundUser.shouldNotBeNull()
    }

    "should provide user by userId" {
        // when
        val foundUser = userDataSource.retrieveUserById(existedUserId)

        // then
        foundUser.shouldNotBeNull()
    }

    "should provide user by username" {
        // when
        val foundUser = userDataSource.retrieveUserByUsername(existedUserUsername)

        // then
        foundUser.shouldNotBeNull()
    }

    "should provide user by email" {
        // when
        val foundUser = userDataSource.retrieveUserByEmail(existedUserEmail)

        // then
        foundUser.shouldNotBeNull()
    }

    "should update user" {
        // given
        val user = userDataSource.retrieveUserById(existedUserId)!!
        val updatingUser = user.copy(
            firstname = "updating"
        )

        // when
        userDataSource.updateUser(updatingUser)
        val foundUser = userDataSource.retrieveUserById(existedUserId)!!

        // then
        updatingUser shouldBe foundUser
    }
})