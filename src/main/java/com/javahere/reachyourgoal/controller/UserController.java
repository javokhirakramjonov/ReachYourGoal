package com.javahere.reachyourgoal.controller;

import com.javahere.reachyourgoal.dto.UserDTO;
import com.javahere.reachyourgoal.dto.UserDTOLogin;
import com.javahere.reachyourgoal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {

        Boolean usernameExists = userService.usernameExists(userDTO.getUsername());
        Boolean emailExists = userService.emailExists(userDTO.getEmail());

        if (usernameExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already taken");
        }

        if (emailExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already taken");
        }

        userService.createUser(userDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User is created successfully");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTOLogin userDTOLogin) {

        Boolean isAuthenticated = userService.login(userDTOLogin);

        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        return ResponseEntity.ok("Login successful");
    }

}
