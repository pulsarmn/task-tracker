package org.pulsar.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.request.TaskEditRequest;
import org.pulsar.tracker.dto.request.TaskStatusUpdateRequest;
import org.pulsar.tracker.dto.response.TaskResponse;
import org.pulsar.tracker.entity.Task;
import org.pulsar.tracker.exception.TaskNotFoundException;
import org.pulsar.tracker.mapper.TaskMapper;
import org.pulsar.tracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Transactional
    public TaskResponse createTask(TaskCreationRequest request) {
        Task task = taskMapper.mapToTask(request);
        task = taskRepository.saveAndFlush(task);
        log.info("New task has been successfully saved");

        return taskMapper.mapToResponse(task);
    }

    @Transactional
    public TaskResponse editTask(UUID taskId, TaskEditRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("The task with the id '%s' was not found".formatted(taskId)));
        applyTaskChanges(task, request);
        task = taskRepository.saveAndFlush(task);
        log.info("The task with the id {} has been successfully updated", taskId);
        return taskMapper.mapToResponse(task);
    }

    private void applyTaskChanges(Task task, TaskEditRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
    }

    @Transactional
    public TaskResponse updateTaskStatus(UUID taskId, TaskStatusUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("The task with the id '%s' was not found".formatted(taskId)));
        task.setStatus(request.status());
        task = taskRepository.saveAndFlush(task);
        return taskMapper.mapToResponse(task);
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
        log.info("Task with id '{}' has been deleted", taskId);
    }
}
