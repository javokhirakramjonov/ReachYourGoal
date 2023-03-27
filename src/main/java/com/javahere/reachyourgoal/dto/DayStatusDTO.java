package com.javahere.reachyourgoal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DayStatusDTO {
    private Integer countTasks;
    private Integer countNotStartedTasks;
    private Integer countInProgressTasks;
    private Integer countCompletedTasks;
}
