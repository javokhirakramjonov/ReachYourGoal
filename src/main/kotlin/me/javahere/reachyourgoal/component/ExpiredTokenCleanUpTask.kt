package me.javahere.reachyourgoal.component

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.javahere.reachyourgoal.datasource.UserDataSource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ExpiredTokenCleanUpTask(
    private val userDataSource: UserDataSource,
) {
    @Scheduled(cron = "0 0 0 * * *")
    suspend fun deleteExpiredTokens() =
        withContext(Dispatchers.IO) {
            val currenTime = System.currentTimeMillis()
            userDataSource.deleteConfirmationTokenBeforeByTime(currenTime)
        }
}
