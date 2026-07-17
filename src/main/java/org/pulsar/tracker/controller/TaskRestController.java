package org.pulsar.tracker.controller;

import lombok.RequiredArgsConstructor;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.request.TaskEditRequest;
import org.pulsar.tracker.dto.request.TaskStatusUpdateRequest;
import org.pulsar.tracker.dto.response.TaskResponse;
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
    ResponseEntity<TaskResponse> createTask(@Validated @RequestBody TaskCreationRequest request) {
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id,
                                            @Validated @RequestBody TaskEditRequest request) {
        TaskResponse response = taskService.editTask(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable UUID id,
                                                  @Validated @RequestBody TaskStatusUpdateRequest request) {
        TaskResponse response = taskService.updateTaskStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
