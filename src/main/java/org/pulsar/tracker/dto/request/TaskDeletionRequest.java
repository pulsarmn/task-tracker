package org.pulsar.tracker.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record TaskDeletionRequest(

        @NotNull
        @Size(max = 128)
        String taskId
) {
}
