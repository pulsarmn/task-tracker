package org.pulsar.tracker.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pulsar.tracker.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.Instant;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(HttpServletRequest request) {
        // TODO: detailed error message
        return build(HttpStatus.BAD_REQUEST, "Invalid body argument", request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occured on path: {}", request.getRequestURI(), e);
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
