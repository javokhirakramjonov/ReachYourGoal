package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dao.DayResponse;
import com.javahere.reachyourgoal.dao.TaskResponse;
import com.javahere.reachyourgoal.entity.Task;
import com.javahere.reachyourgoal.entity.TaskStatus;
import com.javahere.reachyourgoal.mapper.TaskMapper;
import com.javahere.reachyourgoal.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByDate(LocalDate date) {
        taskRepository.save(new Task(0L, "Java Backend", TaskStatus.COMPLETED, date));
        return taskRepository.findByDate(date)
                .stream()
                .map(taskMapper::toTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void createTask(TaskResponse taskResponse) {
        taskRepository.save(taskMapper.toTask(taskResponse));
    }

    @Override
    public void createTasksForPeriod(LocalDate from, LocalDate to, TaskResponse taskResponse) {
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            taskResponse.setDate(date);
            taskRepository.save(taskMapper.toTask(taskResponse));
        }
    }

    @Override
    public List<DayResponse> getDayStatuses(LocalDate from, LocalDate to) {
        List<DayResponse> dayStatuses = new LinkedList<>();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {

            List<Task> tasks = taskRepository.findByDate(date);
            int completed = (int) tasks.stream()
                    .filter(task -> task.getTaskStatus() == TaskStatus.COMPLETED)
                    .count();

            dayStatuses.add(new DayResponse(date, tasks.size(), completed));
        }

        return dayStatuses;
    }

}
