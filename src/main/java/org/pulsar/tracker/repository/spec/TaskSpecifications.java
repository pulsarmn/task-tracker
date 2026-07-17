package org.pulsar.tracker.repository.spec;

import org.pulsar.tracker.entity.Task;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;


public class TaskSpecifications {

    public static Specification<Task> hasDueDateBetween(LocalDate from, LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant toInstant = to.atStartOfDay(ZoneOffset.UTC).toInstant();
            return criteriaBuilder.between(root.get("dueDate"), fromInstant, toInstant);
        };
    }

    public static Specification<Task> hasStatus(Task.Status status) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
