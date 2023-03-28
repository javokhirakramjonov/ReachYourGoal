package com.javahere.reachyourgoal.controller;

import com.javahere.reachyourgoal.dto.TaskDTO;
import com.javahere.reachyourgoal.service.TaskService;
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

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {

        TaskDTO savedTask = taskService.createTask(taskDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTask);
    }

    @PostMapping("/create/period")
    public ResponseEntity<List<TaskDTO>> createDailyTaskForPeriod(
            @RequestParam(name = "frequency") Integer frequency,
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate,
            @RequestBody TaskDTO taskDTO) {

        List<TaskDTO> savedTasks = taskService.createDailyTaskForPeriod(
                taskDTO,
                LocalDate.parse(fromDate),
                LocalDate.parse(toDate),
                frequency
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTasks);
    }

    @GetMapping("/get")
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(name = "date") String date
    ) {
        return ResponseEntity.ok(taskService.getTasks(LocalDate.parse(date)));
    }

}
