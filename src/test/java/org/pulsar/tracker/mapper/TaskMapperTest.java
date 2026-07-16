package org.pulsar.tracker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pulsar.tracker.dto.request.TaskCreationRequest;
import org.pulsar.tracker.dto.response.TaskCreatedResponse;
import org.pulsar.tracker.entity.Task;

import java.time.Instant;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;


public class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    @ParameterizedTest
    @MethodSource("correctCreationTaskRequests")
    void mapToTask_whenCorrectRequest_shouldReturnMappedTask(TaskCreationRequest request) {
        Task task = taskMapper.mapToTask(request);

        assertThat(task.getTitle()).isEqualTo(request.title());
        assertThat(task.getDescription()).isEqualTo(request.description());
        assertThat(task.getDueDate()).isEqualTo(request.dueDate());
    }

    private static Stream<Arguments> correctCreationTaskRequests() {
        String title = "correct-title";
        String description = "correct-description";
        Instant dueDate = Instant.now();
        return Stream.of(
                Arguments.of(new TaskCreationRequest(null, null, null)),
                Arguments.of(new TaskCreationRequest(title, null, null)),
                Arguments.of(new TaskCreationRequest(title, description, null)),
                Arguments.of(new TaskCreationRequest(title, description, dueDate))
        );
    }

    @Test
    void mapToTask_whenNullRequest_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> taskMapper.mapToTask(null))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @MethodSource("correctTasks")
    void mapToResponse_whenCorrectRequest_shouldReturnMappedResponse(Task correctTask) {
        TaskCreatedResponse response = taskMapper.mapToResponse(correctTask);

        assertThat(response.title()).isEqualTo(correctTask.getTitle());
        assertThat(response.description()).isEqualTo(correctTask.getDescription());
        assertThat(response.createdAt()).isEqualTo(correctTask.getCreatedAt());
        assertThat(response.dueDate()).isEqualTo(correctTask.getDueDate());
    }

    private static Stream<Arguments> correctTasks() {
        Task.TaskBuilder builder = Task.builder();
        return Stream.of(
                Arguments.of(builder.build()),
                Arguments.of(builder.title("correct-title").build()),
                Arguments.of(builder.description("correct-description").build()),
                Arguments.of(builder.dueDate(Instant.now()).build()),
                Arguments.of(builder.createdAt(Instant.now()).build())
        );
    }

    @Test
    void mapToResponse_whenNullTask_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> taskMapper.mapToResponse(null))
                .isInstanceOf(NullPointerException.class);
    }
}
