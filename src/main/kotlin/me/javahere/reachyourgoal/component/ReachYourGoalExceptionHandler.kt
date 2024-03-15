package me.javahere.reachyourgoal.component

import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionGroup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import reactor.core.publisher.Mono

@RestControllerAdvice
class ReachYourGoalExceptionHandler : ResponseEntityExceptionHandler() {
    private data class ErrorResponse(
        val message: String,
    )

    @ExceptionHandler
    fun handleExceptions(ex: Exception): Mono<ResponseEntity<*>> {
        val errors =
            when (ex) {
                is RYGException -> listOf(ex)
                is RYGExceptionGroup -> ex.exceptions.toList()
                else -> listOf(RYGException(ex.message))
            }
                .map { ErrorResponse(it.message.orEmpty()) }

        return Mono.just(
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors),
        )
    }
}
