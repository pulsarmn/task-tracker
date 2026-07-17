package org.pulsar.tracker.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pulsar.tracker.dto.response.ErrorResponse;
import org.pulsar.tracker.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Clock;
import java.time.Instant;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(TaskNotFoundException.class)
    ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException e, HttpServletRequest request) {
        log.info("Task was not found", e);
        return build(HttpStatus.NOT_FOUND, "Task was not found", request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ErrorResponse> handleNoResourceFound(HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "No resource found", request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class, NullPointerException.class})
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(HttpServletRequest request) {
        // TODO: detailed error message
        return build(HttpStatus.BAD_REQUEST, "Invalid body argument", request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred on path: {}", request.getRequestURI(), e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status)
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(Instant.now(clock))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
