package com.javahere.reachyourgoal.controller;

import com.javahere.reachyourgoal.dto.DayStatusDTO;
import com.javahere.reachyourgoal.entity.User;
import com.javahere.reachyourgoal.service.TaskService;
import com.javahere.reachyourgoal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/days")
public class HelperController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/statuses")
    public ResponseEntity<List<DayStatusDTO>> getDayStatuses(
            @RequestHeader(name = "username") String username,
            @RequestHeader(name = "password") String password,
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate
    ) {

        User user = userService.getUser(username, password);

        return ResponseEntity.ok(
                taskService.getDayStatuses(
                        LocalDate.parse(fromDate),
                        LocalDate.parse(toDate),
                        user
                )
        );

    }

}
