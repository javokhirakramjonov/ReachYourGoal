package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dto.DayStatusDTO;
import com.javahere.reachyourgoal.dto.TaskDTO;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO> createDailyTaskForPeriod(
            TaskDTO taskDTO,
            LocalDate fromDate,
            LocalDate toDate,
            Integer frequency);

    List<TaskDTO> getTasks(LocalDate date);

    List<DayStatusDTO> getDayStatuses(LocalDate fromDate, LocalDate toDate);
}