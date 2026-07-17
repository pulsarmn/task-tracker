package org.pulsar.tracker.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;


public record TaskEditRequest(

        @NotNull
        @Size(max = 255)
        String title,

        @Size(max = 5000)
        String description,

        Instant dueDate
) {
}
