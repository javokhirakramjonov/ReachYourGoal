package com.javahere.reachyourgoal.dto;

import com.javahere.reachyourgoal.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TaskDTO {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private LocalDate taskDate;
}
