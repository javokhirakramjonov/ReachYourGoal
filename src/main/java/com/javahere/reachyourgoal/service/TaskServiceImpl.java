package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dto.DayStatusDTO;
import com.javahere.reachyourgoal.dto.TaskDTO;
import com.javahere.reachyourgoal.entity.APIResponse;
import com.javahere.reachyourgoal.entity.Task;
import com.javahere.reachyourgoal.entity.User;
import com.javahere.reachyourgoal.mapper.TaskMapper;
import com.javahere.reachyourgoal.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public APIResponse createTask(TaskDTO taskDTO, User user) {

        Task task = taskMapper.toTask(taskDTO);

        task.setUser(user);

        taskRepository.save(task);

        return new APIResponse(201, "Task created");
    }

    @Override
    public APIResponse createDailyTaskForPeriod(
            TaskDTO taskDTO,
            LocalDate fromDate,
            LocalDate toDate,
            Integer frequency,
            User user
    ) {
        Task task = taskMapper.toTask(taskDTO);

        task.setUser(user);

        if (frequency <= 0) {
            throw new RuntimeException("Frequency must be positive");
        }

        if (fromDate.isAfter(toDate)) {
            throw new RuntimeException("Invalid range of dates");
        }

        fromDate
                .datesUntil(toDate, Period.ofDays(frequency))
                .forEach(date -> {
                    task.setTaskDate(date);

                    taskRepository.save(task);
                });

        return new APIResponse(201, "Tasks created");
    }

    @Override
    public List<TaskDTO> getTasks(LocalDate date, User user) {
        return taskRepository
                .findAllByUserIdAndTaskDate(user.getId(), date)
                .stream()
                .map(taskMapper::toTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DayStatusDTO> getDayStatuses(LocalDate fromDate, LocalDate toDate, User user) {
        if (fromDate.isAfter(toDate)) {
            throw new RuntimeException("Invalid range of dates");
        }

        return fromDate
                .datesUntil(toDate, Period.ofDays(1))
                .map(date -> {
                    List<TaskDTO> tasks = getTasks(date, user);

                    AtomicInteger notStarted = new AtomicInteger();
                    AtomicInteger inProgress = new AtomicInteger();
                    AtomicInteger finished = new AtomicInteger();

                    tasks.forEach(task -> {
                        switch (task.getTaskStatus()) {
                            case NOT_STARTED -> notStarted.getAndIncrement();
                            case IN_PROGRESS -> inProgress.getAndIncrement();
                            case FINISHED -> finished.getAndIncrement();
                        }
                    });

                    return new DayStatusDTO(
                            tasks.size(),
                            notStarted.get(),
                            inProgress.get(),
                            finished.get()
                    );
                })
                .collect(Collectors.toList());
    }
}
