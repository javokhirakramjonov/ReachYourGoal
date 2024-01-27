package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.TaskDate
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskDateRepository : CoroutineCrudRepository<TaskDate, Int>
