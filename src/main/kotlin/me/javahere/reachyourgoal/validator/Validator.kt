package me.javahere.reachyourgoal.validator

interface Validator<T> {
    fun validate(input: T): List<String>
}
