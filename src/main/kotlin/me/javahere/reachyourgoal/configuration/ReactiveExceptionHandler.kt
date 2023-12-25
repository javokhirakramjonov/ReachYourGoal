package me.javahere.reachyourgoal.configuration

import kotlinx.coroutines.reactor.awaitSingle
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.server.ServerResponse

@RestControllerAdvice
class ReactiveExceptionHandler {

    @ExceptionHandler(ReachYourGoalException::class)
    suspend fun handleExceptions(exception: ReachYourGoalException): ServerResponse = ServerResponse
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .bodyValue(exception)
        .awaitSingle()

}
