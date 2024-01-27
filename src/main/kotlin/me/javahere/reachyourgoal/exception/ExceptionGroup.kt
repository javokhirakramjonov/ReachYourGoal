package me.javahere.reachyourgoal.exception

class ExceptionGroup(
    vararg val exceptions: RYGException,
) : RuntimeException() {
    constructor(exceptions: List<RYGException>) : this(*exceptions.toTypedArray())
}
