package me.javahere.reachyourgoal.validator

// Lengths
val VALID_FIRSTNAME_LENGTH = 2..20
val VALID_LASTNAME_LENGTH = 2..30
val VALID_USERNAME_LENGTH = 4..20
val VALID_PASSWORD_LENGTH = 4..20
val VALID_TASK_NAME_LENGTH = 2..100
val VALID_TASK_DESCRIPTION_LENGTH = 0..1000

// Patterns
val VALID_NAME_PATTERN = "^[A-Za-z]+\$".toRegex()
val VALID_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
val VALID_USERNAME_PATTERN = "^[a-zA-Z0-9_-]+\$".toRegex()
