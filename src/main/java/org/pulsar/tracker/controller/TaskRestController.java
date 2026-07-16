package org.pulsar.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.pulsar.tracker.dto.TaskCreatedResponse;
import org.pulsar.tracker.dto.TaskCreationRequest;
import org.pulsar.tracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    @PostMapping
    ResponseEntity<TaskCreatedResponse> createTask(@Validated @RequestBody TaskCreationRequest taskCreationRequest) {
        TaskCreatedResponse response = taskService.createTask(taskCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
