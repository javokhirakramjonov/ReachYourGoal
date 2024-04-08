package me.javahere.reachyourgoal.util.extensions

import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.exception.RYGExceptionGroup
import me.javahere.reachyourgoal.validator.Validator

fun <T> Validator<T>.validateAndThrow(input: T) {
    val errors = validate(input)

    if (errors.isNotEmpty()) throw errors.toExceptionGroup()
}

fun List<String>.toExceptionGroup(): RYGExceptionGroup {
    return RYGExceptionGroup(
        this.map {
            RYGException(it)
        },
    )
}
