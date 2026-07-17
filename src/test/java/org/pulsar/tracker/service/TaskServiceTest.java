package org.pulsar.tracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.response.TaskCreatedResponse;
import org.pulsar.tracker.entity.Task;
import org.pulsar.tracker.mapper.TaskMapper;
import org.pulsar.tracker.repository.TaskRepository;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_whenCorrectCreationRequest_shouldSaveTaskAndReturnResponse() {
        TaskCreationRequest request = new TaskCreationRequest("correct-title", "correct-description", Instant.now());
        Task.TaskBuilder builder = Task.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate());
        Task preSavedTask = builder.build();
        Task savedTask = builder.createdAt(Instant.now()).build();
        TaskCreatedResponse expectedResponse = TaskCreatedResponse.builder()
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .dueDate(savedTask.getDueDate())
                .createdAt(savedTask.getCreatedAt())
                .build();

        doReturn(preSavedTask).when(taskMapper).mapToTask(request);
        doReturn(savedTask).when(taskRepository).saveAndFlush(preSavedTask);
        doReturn(expectedResponse).when(taskMapper).mapToResponse(savedTask);

        TaskCreatedResponse actualResponse = taskService.createTask(request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(taskMapper, times(1)).mapToTask(request);
        verify(taskRepository, times(1)).saveAndFlush(preSavedTask);
        verify(taskMapper, times(1)).mapToResponse(savedTask);
    }

    @Test
    void createTask_whenNullRequest_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> taskService.createTask(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("TaskCreationRequest must not be null");
    }

    @Test
    void deleteTask_whenCorrectRequest_shouldDeleteTask() {
        UUID id = UUID.fromString("b59b9772-f3be-47b3-86d8-140f0dca5f42");

        doNothing().when(taskRepository).deleteById(id);

        taskService.deleteTask(id);
        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteTask_whenNullRequest_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> taskService.deleteTask(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Task id must not be null");
    }
}
