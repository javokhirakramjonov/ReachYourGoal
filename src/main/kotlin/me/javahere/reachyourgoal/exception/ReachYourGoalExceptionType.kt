package me.javahere.reachyourgoal.exception

enum class ReachYourGoalExceptionType {
    BAD_REQUEST,
    UN_AUTHORIZED,
    UN_AUTHENTICATED,
    BAD_CREDENTIALS,
    USERNAME_IS_NOT_AVAILABLE,
    EMAIL_IS_NOT_AVAILABLE,
    INVALID_CONFIRM_TOKEN,
    EMAIL_NOT_CONFIRMED,
    ALREADY_EXISTS,
    NOT_FOUND,
    UNDEFINED,
}