package com.javahere.reachyourgoal.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private String name;
    private String description;
    private Long statusId;
}
