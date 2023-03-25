package com.javahere.reachyourgoal.repository;

import com.javahere.reachyourgoal.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByDate(LocalDate date);

}
