package com.javahere.reachyourgoal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTOLogin {
    private String username;
    private String password;
}
