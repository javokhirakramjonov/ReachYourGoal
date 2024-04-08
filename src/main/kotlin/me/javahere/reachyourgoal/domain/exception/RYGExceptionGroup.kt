package me.javahere.reachyourgoal.domain.exception

class RYGExceptionGroup(
    vararg val exceptions: RYGException,
) : RuntimeException() {
    constructor(exceptions: List<RYGException>) : this(*exceptions.toTypedArray())
}
