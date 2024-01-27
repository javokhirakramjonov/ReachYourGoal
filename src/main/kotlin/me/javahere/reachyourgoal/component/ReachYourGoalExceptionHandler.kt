package me.javahere.reachyourgoal.component

import me.javahere.reachyourgoal.exception.ExceptionGroup
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import reactor.core.publisher.Mono

@RestControllerAdvice
class ReachYourGoalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler
    fun handleExceptions(ex: Exception): Mono<ResponseEntity<*>> {
        val errors =
            when (ex) {
                is ExceptionGroup -> ex.exceptions.toList()
                else -> listOf(RYGException(RYGExceptionType.UNDEFINED, ex.message))
            }

        return Mono.just(
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors),
        )
    }
}
