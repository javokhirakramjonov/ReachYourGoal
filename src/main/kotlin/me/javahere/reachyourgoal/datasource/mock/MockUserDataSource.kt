package me.javahere.reachyourgoal.datasource.mock

import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.domain.UserUnConfirmed
import me.javahere.reachyourgoal.util.MockConstants.USER_EMAIL
import me.javahere.reachyourgoal.util.MockConstants.USER_FIRSTNAME
import me.javahere.reachyourgoal.util.MockConstants.USER_ID
import me.javahere.reachyourgoal.util.MockConstants.USER_LASTNAME
import me.javahere.reachyourgoal.util.MockConstants.USER_PASSWORD
import me.javahere.reachyourgoal.util.MockConstants.USER_ROLE
import me.javahere.reachyourgoal.util.MockConstants.USER_USERNAME
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MockUserDataSource() : UserDataSource {

    private val users = mutableListOf(
        User(
            id = USER_ID,
            firstname = USER_FIRSTNAME,
            lastname = USER_LASTNAME,
            username = USER_USERNAME,
            email = USER_EMAIL,
            password = USER_PASSWORD,
            role = USER_ROLE
        )
    )

    private val unConfirmedUsers = mutableListOf<UserUnConfirmed>()

    override suspend fun createUser(user: User): User {
        val userWithId = if (user.id == null) user.copy(id = UUID.randomUUID()) else user

        users.add(userWithId)

        return userWithId
    }

    override suspend fun createUnConfirmedUser(user: UserUnConfirmed): UserUnConfirmed {
        TODO()
    }

    override suspend fun retrieveUserById(userId: UUID): User? {
        return users.firstOrNull { it.id == userId }
    }

    override suspend fun retrieveUserByUsername(username: String): User? {
        return users.firstOrNull { it.username == username }
    }

    override suspend fun retrieveUserByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override suspend fun retrieveUnConfirmedUserByUsername(username: String): UserUnConfirmed? {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveUnConfirmedUserByEmail(email: String): UserUnConfirmed? {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveUnConfirmedUserByToken(token: String): UserUnConfirmed? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User): User {
        val foundUser = users.firstOrNull { it.id == user.id }

        if (foundUser == null) return createUser(user)

        val index = users.indexOf(foundUser)

        users[index] = user

        return user
    }

    override suspend fun deleteUserById(userId: UUID) {
        users.removeIf { it.id == userId }
    }
}