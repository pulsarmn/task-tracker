package org.pulsar.tracker.dto;

import lombok.Builder;
import java.time.Instant;


@Builder
public record TaskCreatedResponse(
        String title,
        String description,
        Instant dueDate,
        Instant createdAt
) {
}
