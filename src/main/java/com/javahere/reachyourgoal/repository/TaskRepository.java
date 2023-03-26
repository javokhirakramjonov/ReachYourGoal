package com.javahere.reachyourgoal.repository;

import com.javahere.reachyourgoal.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
