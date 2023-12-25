package me.javahere.reachyourgoal.exception

sealed class ReachYourGoalExceptionType(val message: String? = null) {
    data object BadRequest : ReachYourGoalExceptionType()
    data object UnAuthorized : ReachYourGoalExceptionType()
    data object UnAuthenticated : ReachYourGoalExceptionType()
    data object BadCredentials : ReachYourGoalExceptionType()

    data class AlreadyExists(val msg: String) : ReachYourGoalExceptionType(msg)
    data class NotFound(val msg: String) : ReachYourGoalExceptionType(msg)
}