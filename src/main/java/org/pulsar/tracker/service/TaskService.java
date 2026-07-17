package org.pulsar.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pulsar.tracker.dto.request.*;
import org.pulsar.tracker.dto.response.TaskResponse;
import org.pulsar.tracker.entity.Task;
import org.pulsar.tracker.exception.TaskNotFoundException;
import org.pulsar.tracker.mapper.TaskMapper;
import org.pulsar.tracker.repository.TaskRepository;
import org.pulsar.tracker.repository.spec.TaskSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public List<TaskResponse> getAll(TaskFilter filter) {
        Specification<Task> specification = mapToSpecification(filter);
        return taskRepository.findAll(specification)
                .stream()
                .map(taskMapper::mapToResponse)
                .collect(Collectors.toList());

    }

    private Specification<Task> mapToSpecification(TaskFilter filter) {
        Specification<Task> specification = Specification.unrestricted();
        if (filter.status() != null) {
            specification = specification.and(TaskSpecifications.hasStatus(filter.status()));
        }
        return specification.and(TaskSpecifications.hasDueDateBetween(filter.dateFrom(), filter.dateTo()));
    }

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
