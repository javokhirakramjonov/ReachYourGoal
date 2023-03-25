package com.javahere.reachyourgoal.controller;

import com.javahere.reachyourgoal.dao.DayResponse;
import com.javahere.reachyourgoal.dao.TaskResponse;
import com.javahere.reachyourgoal.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping(value = "/{date}")
    public ResponseEntity<List<TaskResponse>> getTasksByDate(@PathVariable String date) {
        return ResponseEntity.ok(taskService.getTasksByDate(LocalDate.parse(date)));
    }

    @GetMapping("/dayStatuses")
    public ResponseEntity<List<DayResponse>> getDayStatuses() {
        //@RequestParam("from") String from, @RequestParam("to") String to
        LocalDate fromDate = LocalDate.parse("2023-03-19");
        LocalDate toDate = LocalDate.parse("2023-03-19");
        return ResponseEntity.ok(taskService.getDayStatuses(fromDate, toDate));
    }
}
