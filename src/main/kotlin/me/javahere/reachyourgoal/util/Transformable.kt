package me.javahere.reachyourgoal.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Transformable<OUTPUT> {

	fun transform(): OUTPUT

}

fun <OUTPUT> List<Transformable<OUTPUT>>.transformCollection(): List<OUTPUT> = map(Transformable<OUTPUT>::transform)
fun <OUTPUT> Flow<Transformable<OUTPUT>>.transformCollection(): Flow<OUTPUT> = map(Transformable<OUTPUT>::transform)
