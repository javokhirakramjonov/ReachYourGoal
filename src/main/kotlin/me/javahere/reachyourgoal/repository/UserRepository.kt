package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface UserRepository : CoroutineCrudRepository<User, UUID>