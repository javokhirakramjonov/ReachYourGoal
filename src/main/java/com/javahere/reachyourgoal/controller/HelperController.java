package com.javahere.reachyourgoal.controller;

import com.javahere.reachyourgoal.dto.DayStatusDTO;
import com.javahere.reachyourgoal.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/days")
public class HelperController {

    private final TaskService taskService;

    @GetMapping("/statuses")
    public ResponseEntity<List<DayStatusDTO>> getDayStatuses(
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate
    ) {

        return ResponseEntity.ok(
                taskService.getDayStatuses(
                        LocalDate.parse(fromDate),
                        LocalDate.parse(toDate)
                )
        );

    }

}
