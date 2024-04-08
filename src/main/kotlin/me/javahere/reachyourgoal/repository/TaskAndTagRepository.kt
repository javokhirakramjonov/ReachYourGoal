package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.TaskAndTagId
import me.javahere.reachyourgoal.domain.entity.TaskAndTag
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskAndTagRepository : CoroutineCrudRepository<TaskAndTag, TaskAndTagId>
