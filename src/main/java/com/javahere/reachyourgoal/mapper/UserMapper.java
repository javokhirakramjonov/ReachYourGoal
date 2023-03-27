package com.javahere.reachyourgoal.mapper;

import com.javahere.reachyourgoal.dto.UserDTO;
import com.javahere.reachyourgoal.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserMapper {
    public User toUser(UserDTO userDTO) {

        User user = new User();

        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setJoinedAt(LocalDate.now());

        return user;
    }
}
