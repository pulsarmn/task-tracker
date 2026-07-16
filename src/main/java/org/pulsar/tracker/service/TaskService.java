package org.pulsar.tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pulsar.tracker.dto.TaskCreatedResponse;
import org.pulsar.tracker.dto.TaskCreationRequest;
import org.pulsar.tracker.entity.Task;
import org.pulsar.tracker.mapper.TaskMapper;
import org.pulsar.tracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}
