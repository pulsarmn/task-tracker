package org.pulsar.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.request.TaskEditRequest;
import org.pulsar.tracker.dto.response.TaskCreatedResponse;
import org.pulsar.tracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskRestController {

    private final TaskService taskService;

    @PostMapping
    ResponseEntity<TaskCreatedResponse> createTask(@Validated @RequestBody TaskCreationRequest taskCreationRequest) {
        TaskCreatedResponse response = taskService.createTask(taskCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<TaskCreatedResponse> updateTask(@PathVariable UUID id,
                                                   @Validated @RequestBody TaskEditRequest taskEditRequest) {
        TaskCreatedResponse response = taskService.editTask(id, taskEditRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
