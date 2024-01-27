package me.javahere.reachyourgoal.exception

class RYGException(
    val type: RYGExceptionType,
    override val message: String? = null,
) : RuntimeException()
