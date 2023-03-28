package com.javahere.reachyourgoal.service.impl;

import com.javahere.reachyourgoal.dto.DayStatusDTO;
import com.javahere.reachyourgoal.dto.TaskDTO;
import com.javahere.reachyourgoal.entity.Task;
import com.javahere.reachyourgoal.entity.User;
import com.javahere.reachyourgoal.mapper.TaskMapper;
import com.javahere.reachyourgoal.repository.TaskRepository;
import com.javahere.reachyourgoal.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {

        User user = getCurrentUser();

        Task task = taskMapper.toTask(taskDTO);

        task.setUser(user);

        return taskMapper.toTaskDTO(taskRepository.save(task));
    }

    @Override
    public List<TaskDTO> createDailyTaskForPeriod(
            TaskDTO taskDTO,
            LocalDate fromDate,
            LocalDate toDate,
            Integer frequency
    ) {
        User user = getCurrentUser();

        Task task = taskMapper.toTask(taskDTO);

        task.setUser(user);

        LinkedList<Task> tasks = new LinkedList<>();

        fromDate
                .datesUntil(toDate, Period.ofDays(frequency))
                .forEach(date -> {
                    task.setTaskDate(date);

                    tasks.add(taskRepository.save(task));
                });

        return tasks.stream()
                .map(taskMapper::toTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasks(LocalDate date) {
        User user = getCurrentUser();
        return taskRepository
                .findAllByUserIdAndTaskDate(user.getId(), date)
                .stream()
                .map(taskMapper::toTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DayStatusDTO> getDayStatuses(LocalDate fromDate, LocalDate toDate) {

        return fromDate
                .datesUntil(toDate, Period.ofDays(1))
                .map(date -> {
                    List<TaskDTO> tasks = getTasks(date);

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

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
