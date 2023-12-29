package me.javahere.reachyourgoal.exception

class ExceptionResponse(
    vararg val exceptions: ReachYourGoalException
) : RuntimeException() {
    constructor(exceptions: List<ReachYourGoalException>) : this(*exceptions.toTypedArray())
}