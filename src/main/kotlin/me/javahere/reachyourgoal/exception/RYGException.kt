package me.javahere.reachyourgoal.exception

class RYGException(
    override val message: String? = "Something went wrong",
) : RuntimeException()
