package me.javahere.reachyourgoal.util.extensions

import kotlinx.coroutines.reactive.collect
import reactor.core.publisher.Mono

suspend fun Mono<*>.collect() = this.collect { }
