package com.javahere.reachyourgoal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponse {
    private Integer status;
    private String message;
}
