package org.pulsar.tracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.request.TaskEditRequest;
import org.pulsar.tracker.dto.response.TaskCreatedResponse;
import org.pulsar.tracker.entity.Task;
import org.pulsar.tracker.exception.TaskNotFoundException;
import org.pulsar.tracker.mapper.TaskMapper;
import org.pulsar.tracker.repository.TaskRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
    }

    @Test
    void deleteTask_whenCorrectRequest_shouldDeleteTask() {
        UUID id = UUID.fromString("b59b9772-f3be-47b3-86d8-140f0dca5f42");

        doNothing().when(taskRepository).deleteById(id);

        taskService.deleteTask(id);
        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    void editTask_whenCorrectRequest_shouldUpdateTask() {
        UUID id = UUID.fromString("b59b9772-f3be-47b3-86d8-140f0dca5f42");
        TaskEditRequest request = new TaskEditRequest("New title", "New description", Instant.now()); // I know it's better not to do this with an Instant object :)
        Task oldTask = Task.builder()
                .id(id)
                .title("Old title")
                .description("Old description")
                .dueDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();
        Task newTask = Task.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate())
                .build();
        TaskCreatedResponse expectedResponse = TaskCreatedResponse.builder()
                .id(newTask.getId().toString())
                .title(newTask.getTitle())
                .description(newTask.getDescription())
                .dueDate(newTask.getDueDate())
                .build();

        doReturn(Optional.of(oldTask)).when(taskRepository).findById(id);
        doReturn(newTask).when(taskRepository).saveAndFlush(newTask);
        doReturn(expectedResponse).when(taskMapper).mapToResponse(newTask);

        TaskCreatedResponse actualResponse = taskService.editTask(id, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void editTask_whenTaskDoesNotExist_shouldThrowTaskNotFoundException() {
        UUID id = UUID.fromString("b59b9772-f3be-47b3-86d8-140f0dca5f42");
        TaskEditRequest request = new TaskEditRequest("New title", "New Description", Instant.now()); // And here :)

        doReturn(Optional.empty()).when(taskRepository).findById(id);

        assertThatThrownBy(() -> taskService.editTask(id, request))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("The task the with id '%s' was not found".formatted(id));
    }
}
