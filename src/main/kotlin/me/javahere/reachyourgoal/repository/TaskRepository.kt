package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.Task
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface TaskRepository : CoroutineCrudRepository<Task, UUID>