package org.pulsar.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.request.TaskEditRequest;
import org.pulsar.tracker.dto.response.TaskCreatedResponse;
import org.pulsar.tracker.entity.Task;
import org.pulsar.tracker.exception.TaskNotFoundException;
import org.pulsar.tracker.mapper.TaskMapper;
import org.pulsar.tracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Transactional
    public TaskCreatedResponse createTask(TaskCreationRequest request) {
        Task task = taskMapper.mapToTask(request);
        task = taskRepository.saveAndFlush(task);
        log.info("New task has been successfully saved");

        return taskMapper.mapToResponse(task);
    }

    @Transactional
    public TaskCreatedResponse editTask(UUID taskId, TaskEditRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("The task the with id '{}' was not found"));
        applyTaskChanges(task, request);
        task = taskRepository.saveAndFlush(task);
        return taskMapper.mapToResponse(task);
    }

    private void applyTaskChanges(Task task, TaskEditRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
        log.info("Task with id '{}' has been deleted", taskId);
    }
}
