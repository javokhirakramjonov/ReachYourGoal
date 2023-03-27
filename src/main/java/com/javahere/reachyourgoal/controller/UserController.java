package com.javahere.reachyourgoal.controller;

import com.javahere.reachyourgoal.dto.UserDTO;
import com.javahere.reachyourgoal.dto.UserDTOLogin;
import com.javahere.reachyourgoal.entity.APIResponse;
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
    public ResponseEntity<APIResponse> signUp(@RequestBody UserDTO userDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userDTO));
    }

    @GetMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody UserDTOLogin userDTOLogin) {
        return ResponseEntity.ok(userService.login(userDTOLogin));
    }

}
