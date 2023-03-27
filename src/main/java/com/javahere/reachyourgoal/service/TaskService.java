package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dto.DayStatusDTO;
import com.javahere.reachyourgoal.dto.TaskDTO;
import com.javahere.reachyourgoal.entity.APIResponse;
import com.javahere.reachyourgoal.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    APIResponse createTask(TaskDTO taskDTO);

    APIResponse createDailyTaskForPeriod(
            TaskDTO taskDTO,
            LocalDate fromDate,
            LocalDate toDate,
            Integer frequency,
            User user);

    List<TaskDTO> getTasks(LocalDate date, User user);

    List<DayStatusDTO> getDayStatuses(LocalDate fromDate, LocalDate toDate, User user);
}