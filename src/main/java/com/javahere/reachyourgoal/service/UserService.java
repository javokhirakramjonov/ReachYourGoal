package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dto.UserDTO;
import com.javahere.reachyourgoal.dto.UserDTOLogin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Boolean usernameExists(String username);

    Boolean emailExists(String email);

    UserDTO createUser(UserDTO userDTO);

    Boolean login(UserDTOLogin userDTOLogin);
}
