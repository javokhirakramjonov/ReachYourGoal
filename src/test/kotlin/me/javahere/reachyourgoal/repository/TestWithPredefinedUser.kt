package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.PostgresExtension
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.mockUser
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.properties.Delegates

@ExtendWith(PostgresExtension::class)
abstract class TestWithPredefinedUser(
    private val userRepository: UserRepository,
) {
    var predefinedUser by Delegates.notNull<User>()

    init {
        runBlocking {
            val user = mockUser()
            predefinedUser = userRepository.addUser(user)
        }
    }
}
