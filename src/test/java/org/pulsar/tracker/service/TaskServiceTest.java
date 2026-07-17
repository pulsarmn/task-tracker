package org.pulsar.tracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.request.TaskEditRequest;
import org.pulsar.tracker.dto.request.TaskFilter;
import org.pulsar.tracker.dto.request.TaskStatusUpdateRequest;
import org.pulsar.tracker.dto.response.TaskResponse;
import org.pulsar.tracker.entity.Task;
import org.pulsar.tracker.exception.TaskNotFoundException;
import org.pulsar.tracker.mapper.TaskMapper;
import org.pulsar.tracker.repository.TaskRepository;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    void getAll_whenPassedCorrectFilter_shouldReturnFilteredTasks() {
        TaskFilter filter = new TaskFilter(LocalDate.of(2026, Month.JULY, 15), LocalDate.of(2026, Month.JULY, 20), Task.Status.INCOMPLETE);
        List<Task> expectedTasks = getTasks();

        doReturn(expectedTasks).when(taskRepository).findAll(any(Specification.class));

        List<TaskResponse> actualTasks = taskService.getAll(filter);

        assertThat(actualTasks).isNotNull();
        assertThat(actualTasks).hasSize(expectedTasks.size());
    }

    private List<Task> getTasks() {
        return List.of(
                new Task(UUID.randomUUID(), "First title", "First Description", null, null, null, null),
                new Task(UUID.randomUUID(), "Second title", "Second Description", null, null, null, null),
                new Task(UUID.randomUUID(), "Third title", "Third Description", null, null, null, null)
        );
    }

    @Test
    void createTask_whenCorrectCreationRequest_shouldSaveTaskAndReturnResponse() {
        TaskCreationRequest request = new TaskCreationRequest("correct-title", "correct-description", Instant.now());
        Task.TaskBuilder builder = Task.builder()
                .title(request.title())
                .description(request.description())
                .dueDate(request.dueDate());
        Task preSavedTask = builder.build();
        Task savedTask = builder.createdAt(Instant.now()).build();
        TaskResponse expectedResponse = TaskResponse.builder()
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .dueDate(savedTask.getDueDate())
                .createdAt(savedTask.getCreatedAt())
                .build();

        doReturn(preSavedTask).when(taskMapper).mapToTask(request);
        doReturn(savedTask).when(taskRepository).saveAndFlush(preSavedTask);
        doReturn(expectedResponse).when(taskMapper).mapToResponse(savedTask);

        TaskResponse actualResponse = taskService.createTask(request);

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
        TaskResponse expectedResponse = TaskResponse.builder()
                .id(newTask.getId().toString())
                .title(newTask.getTitle())
                .description(newTask.getDescription())
                .dueDate(newTask.getDueDate())
                .build();

        doReturn(Optional.of(oldTask)).when(taskRepository).findById(id);
        doReturn(newTask).when(taskRepository).saveAndFlush(newTask);
        doReturn(expectedResponse).when(taskMapper).mapToResponse(newTask);

        TaskResponse actualResponse = taskService.editTask(id, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void editTask_whenTaskDoesNotExist_shouldThrowTaskNotFoundException() {
        UUID id = UUID.fromString("b59b9772-f3be-47b3-86d8-140f0dca5f42");
        TaskEditRequest request = new TaskEditRequest("New title", "New Description", Instant.now()); // And here :)

        doReturn(Optional.empty()).when(taskRepository).findById(id);

        assertThatThrownBy(() -> taskService.editTask(id, request))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("The task with the id '%s' was not found".formatted(id));
    }

    @Test
    void updateTaskStatus_whenCorrectRequest_shouldUpdateTaskStatus() {
        UUID id = UUID.fromString("b59b9772-f3be-47b3-86d8-140f0dca5f42");
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(Task.Status.COMPLETED);
        Task.TaskBuilder builder = Task.builder()
                .id(id)
                .title("Some title")
                .description("Some description");
        Task oldTask = builder.status(Task.Status.INCOMPLETE).build();
        Task newTask = builder.status(Task.Status.COMPLETED).build();
        TaskResponse expectedResponse = TaskResponse.builder()
                .id(newTask.getId().toString())
                .title(newTask.getTitle())
                .description(newTask.getDescription())
                .status(newTask.getStatus().toString())
                .build();

        doReturn(Optional.of(oldTask)).when(taskRepository).findById(id);
        doReturn(newTask).when(taskRepository).saveAndFlush(newTask);
        doReturn(expectedResponse).when(taskMapper).mapToResponse(newTask);

        TaskResponse actualResponse = taskService.updateTaskStatus(id, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void updateTaskStatus_whenTaskDoesNotExist_shouldThrowTaskNotFoundException() {
        UUID id = UUID.fromString("b59b9772-f3be-47b3-86d8-140f0dca5f42");
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(Task.Status.COMPLETED);

        doReturn(Optional.empty()).when(taskRepository).findById(id);

        assertThatThrownBy(() -> taskService.updateTaskStatus(id, request))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("The task with the id '%s' was not found".formatted(id));
    }
}
