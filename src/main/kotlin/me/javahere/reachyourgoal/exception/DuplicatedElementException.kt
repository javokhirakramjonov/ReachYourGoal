package me.javahere.reachyourgoal.exception

class DuplicatedElementException(
    reason: String? = null
) : RuntimeException(
    when {
        reason != null -> reason
        else -> "Element is already exists"
    }
)