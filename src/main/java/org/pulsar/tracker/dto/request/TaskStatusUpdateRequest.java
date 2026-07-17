package org.pulsar.tracker.dto.request;

import jakarta.validation.constraints.NotNull;
import org.pulsar.tracker.entity.Task;


public record TaskStatusUpdateRequest(

        @NotNull
        Task.Status status
) {
}
