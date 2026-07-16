package org.pulsar.tracker.mapper;

import org.pulsar.tracker.dto.TaskCreatedResponse;
import org.pulsar.tracker.dto.TaskCreationRequest;
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

    public TaskCreatedResponse mapToResponse(Task task) {
        return TaskCreatedResponse.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
