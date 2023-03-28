package com.javahere.reachyourgoal.service.impl;

import com.javahere.reachyourgoal.config.PasswordEncoder;
import com.javahere.reachyourgoal.dto.UserDTO;
import com.javahere.reachyourgoal.dto.UserDTOLogin;
import com.javahere.reachyourgoal.entity.User;
import com.javahere.reachyourgoal.mapper.UserMapper;
import com.javahere.reachyourgoal.repository.UserRepository;
import com.javahere.reachyourgoal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Boolean usernameExists(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null;
    }

    @Override
    public Boolean emailExists(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(userDTO.getPassword()));

        User user = userMapper.toUser(userDTO);

        return userMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public Boolean login(UserDTOLogin userDTOLogin) {

        User user = userRepository.findByUsername(userDTOLogin.getUsername()).orElse(null);

        if (user == null) {
            return false;
        }

        return passwordEncoder
                .bCryptPasswordEncoder()
                .matches(userDTOLogin.getPassword(), user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username or password is invalid"));
    }
}
