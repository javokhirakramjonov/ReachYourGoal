package me.javahere.reachyourgoal.localize

enum class MessagesEnum(val key: String) {
    //Response messages
    NEW_EMAIL_CONFIRMATION_SENT(key = "new.email.confirmation.sent"),
    REGISTRATION_EMAIL_CONFIRMATION_SENT(key = "registration.email.confirmation.sent"),

    //Error messages
    TASK_NOT_FOUND_EXCEPTION(key = "task.not.found.exception"),
    USER_NOT_FOUND_FOR_ID_EXCEPTION(key = "user.not.found.for.id.exception"),
    USER_NOT_FOUND_FOR_EMAIL_EXCEPTION(key = "user.not.found.for.email.exception"),
    USER_NOT_FOUND_FOR_USERNAME_EXCEPTION(key = "user.not.found.for.username.exception"),
    USERNAME_ALREADY_EXISTS_EXCEPTION(key = "username.already.exists.exception"),
    EMAIL_ALREADY_EXISTS_EXCEPTION(key = "email.already.exists.exception"),
    EMAIL_ALREADY_ASSIGNED_TO_CURRENT_USER_EXCEPTION(key = "email.already.assigned.to.current.user.exception"),
}
