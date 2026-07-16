package org.pulsar.tracker.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;


@Getter
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final Instant timestamp;
    private final String path;

    public ErrorResponse(HttpStatus status, String error, String message, Instant timestamp, String path) {
        this.status = status.value();
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }

    // Manual Builder as you need to be sure that correct HTTP status being transmitted
    public static class Builder {
        private HttpStatus status;
        private String error;
        private String message;
        private Instant timestamp;
        private String path;

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(status, error, message, timestamp, path);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
