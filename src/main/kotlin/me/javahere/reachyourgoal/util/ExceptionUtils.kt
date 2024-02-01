package me.javahere.reachyourgoal.util

import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionGroup
import me.javahere.reachyourgoal.exception.RYGExceptionType
import me.javahere.reachyourgoal.validator.Validator

fun <T> Validator<T>.validateAndThrow(input: T) {
    val errors = validate(input)

    if (errors.isNotEmpty()) throw errors.toExceptionGroup()
}

fun List<String>.toExceptionGroup(): RYGExceptionGroup {
    return RYGExceptionGroup(
        this.map {
            RYGException(RYGExceptionType.INVALID, it)
        },
    )
}
