package com.javahere.reachyourgoal.controller;

import com.javahere.reachyourgoal.dto.TaskDTO;
import com.javahere.reachyourgoal.entity.APIResponse;
import com.javahere.reachyourgoal.entity.User;
import com.javahere.reachyourgoal.service.TaskService;
import com.javahere.reachyourgoal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/task")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse> createTask(@RequestBody TaskDTO taskDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(taskDTO));
    }

    @PostMapping("/create/period")
    public ResponseEntity<APIResponse> createDailyTaskForPeriod(
            @RequestHeader(name = "username") String username,
            @RequestHeader(name = "password") String password,
            @RequestParam(name = "frequency") Integer frequency,
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate,
            @RequestBody TaskDTO taskDTO) {
        User user = userService.getUser(username, password);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        taskService.createDailyTaskForPeriod(
                                taskDTO,
                                LocalDate.parse(fromDate),
                                LocalDate.parse(toDate),
                                frequency,
                                user
                        )
                );
    }

    @GetMapping("/get")
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestHeader(name = "username") String username,
            @RequestHeader(name = "password") String password,
            @RequestParam(name = "date") String date
    ) {
        User user = userService.getUser(username, password);

        return ResponseEntity.ok(taskService.getTasks(LocalDate.parse(date), user));
    }

}
