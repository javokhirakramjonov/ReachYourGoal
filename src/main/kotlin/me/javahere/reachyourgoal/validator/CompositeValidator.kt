package me.javahere.reachyourgoal.validator

open class CompositeValidator<T> : Validator<T> {
    private val validators = mutableListOf<Validator<T>>()

    fun add(validator: Validator<T>) {
        validators += validator
    }

    fun addAll(validators: List<Validator<T>>) {
        this.validators.addAll(validators)
    }

    operator fun plusAssign(validator: Validator<T>) {
        validators += validator
    }

    override fun validate(input: T): List<String> {
        return validators
            .asSequence()
            .map { it.validate(input) }
            .filter(List<String>::isNotEmpty)
            .flatten()
            .toList()
    }
}
