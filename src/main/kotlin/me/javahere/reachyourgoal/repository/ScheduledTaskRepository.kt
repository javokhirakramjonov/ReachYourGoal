package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.ScheduledTask
import me.javahere.reachyourgoal.domain.ScheduledTaskId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ScheduledTaskRepository : CoroutineCrudRepository<ScheduledTask, ScheduledTaskId>
