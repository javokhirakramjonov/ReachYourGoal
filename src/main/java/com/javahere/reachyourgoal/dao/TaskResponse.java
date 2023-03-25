package com.javahere.reachyourgoal.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private String name;
    private String taskStatus;
    private LocalDate date;
}
