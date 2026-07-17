package org.pulsar.tracker.mapper;

import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.response.TaskResponse;
import org.pulsar.tracker.entity.Task;
import org.springframework.stereotype.Component;


@Component
public class TaskMapper {

    public Task mapToTask(TaskCreationRequest request) {
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .build();
    }

    public TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId() == null ? null : task.getId().toString())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus() == null ? Task.Status.INCOMPLETE.toString() : task.getStatus().toString())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
