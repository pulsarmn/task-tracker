package org.pulsar.tracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;


public record TaskCreationRequest(

        @NotNull
        @Size(min = 1, max = 255)
        String title,

        @Size(max = 5000)
        String description,

        Instant dueDate
) {
}
