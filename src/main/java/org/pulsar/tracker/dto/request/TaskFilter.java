package org.pulsar.tracker.dto.request;

import jakarta.validation.constraints.AssertTrue;
import org.pulsar.tracker.entity.Task;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public record TaskFilter(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dateFrom,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dateTo,

        Task.Status status
) {

    @AssertTrue
    public boolean isValidDateRange() {
        return dateFrom != null && dateTo != null && !dateFrom.isAfter(dateTo);
    }
}
