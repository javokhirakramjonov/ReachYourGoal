package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dto.UserDTO;
import com.javahere.reachyourgoal.dto.UserDTOLogin;
import com.javahere.reachyourgoal.entity.APIResponse;
import com.javahere.reachyourgoal.entity.User;

public interface UserService {

    APIResponse createUser(UserDTO userDTO);

    User getUser(String username, String password);

    APIResponse login(UserDTOLogin userDTOLogin);
}
