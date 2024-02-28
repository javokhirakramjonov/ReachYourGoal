package me.javahere.reachyourgoal.component

import me.javahere.reachyourgoal.repository.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class UnconfirmedUserRemover(
    private val userRepository: UserRepository,
) {
    private companion object {
        const val VALID_DAYS_TO_CONFIRM = 1L
    }

    @Scheduled(cron = "0 0 0 * * *")
    suspend fun deleteExpiredTokens() {
        val today = LocalDate.now()
        userRepository.deleteUnconfirmedUsersBefore(today.minusDays(VALID_DAYS_TO_CONFIRM))
    }
}
