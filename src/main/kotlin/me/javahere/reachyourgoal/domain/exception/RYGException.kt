package me.javahere.reachyourgoal.domain.exception

class RYGException(
    override val message: String? = "Something went wrong",
) : RuntimeException()
