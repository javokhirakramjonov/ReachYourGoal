package com.javahere.reachyourgoal.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DayResponse {
    private LocalDate date;
    private Integer allTasks;
    private Integer completed;
}
