package me.javahere.reachyourgoal.util

import java.util.UUID

val String.Companion.EMPTY: String
    get() = ""

fun String.toUUID() = UUID.fromString(this)
