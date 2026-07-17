package org.pulsar.tracker.dto.response;

import lombok.Builder;
import java.time.Instant;


@Builder
public record TaskResponse(
        String id,
        String title,
        String description,
        String status,
        Instant dueDate,
        Instant createdAt
) {
}
