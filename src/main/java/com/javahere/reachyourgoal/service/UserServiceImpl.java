package com.javahere.reachyourgoal.service;

import com.javahere.reachyourgoal.dto.UserDTO;
import com.javahere.reachyourgoal.dto.UserDTOLogin;
import com.javahere.reachyourgoal.entity.APIResponse;
import com.javahere.reachyourgoal.entity.User;
import com.javahere.reachyourgoal.mapper.UserMapper;
import com.javahere.reachyourgoal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public APIResponse createUser(UserDTO userDTO) {

        User user1 = userRepository.findByUsername(userDTO.getUsername()).orElse(null);
        User user2 = userRepository.findByEmail(userDTO.getEmail()).orElse(null);

        if (user1 != null) {
            throw new RuntimeException("There is user with this username");
        }

        if (user2 != null) {
            throw new RuntimeException("There is user with this email");
        }

        User user = userMapper.toUser(userDTO);

        userRepository.save(user);

        return new APIResponse(201, "User created");
    }

    @Override
    public APIResponse login(UserDTOLogin userDTOLogin) {
        getUser(userDTOLogin.getUsername(), userDTOLogin.getPassword());

        return new APIResponse(200, "success");
    }

    @Override
    public User getUser(String username, String password) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Username or Password is invalid");
        }

        return user;
    }
}
