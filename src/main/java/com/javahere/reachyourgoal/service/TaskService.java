package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dao.DayResponse;
import com.javahere.reachyourgoal.dao.TaskResponse;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    List<TaskResponse> getAllTasks();

    List<TaskResponse> getTasksByDate(LocalDate date);

    void createTask(TaskResponse taskResponse);

    void createTasksForPeriod(LocalDate from, LocalDate to, TaskResponse task);

    List<DayResponse> getDayStatuses(LocalDate from, LocalDate to);

}
