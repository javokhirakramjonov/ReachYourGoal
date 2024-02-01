package me.javahere.reachyourgoal.exception

enum class RYGExceptionType {
    BAD_REQUEST,
    UN_AUTHENTICATED,
    BAD_CREDENTIALS,
    USERNAME_IS_NOT_AVAILABLE,
    EMAIL_IS_NOT_AVAILABLE,
    INVALID_CONFIRM_TOKEN,
    EMAIL_NOT_CONFIRMED,
    ALREADY_EXISTS,
    NOT_FOUND,
    UNDEFINED,
    INTERNAL_ERROR,
    INVALID,
}
