package com.javahere.reachyourgoal.mapper;

import com.javahere.reachyourgoal.dto.TaskDTO;
import com.javahere.reachyourgoal.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toTask(TaskDTO taskDTO) {
        Task task = new Task();

        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setTaskDate(taskDTO.getTaskDate());

        return task;
    }

    public TaskDTO toTaskDTO(Task task) {
        return new TaskDTO(
                task.getName(),
                task.getDescription(),
                task.getTaskStatus(),
                task.getTaskDate()
        );
    }
}
