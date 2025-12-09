package org.senai.cantina_vidal.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationErrors(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Erro desconhecido",
                        (msg1, msg2) -> msg1 + "; " + msg2
                ));

        log.error("Erro completo capturado: ", e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de Validação", e.getMessage(), request, errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleEntityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        log.error("Erro completo capturado: ", e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", e.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        log.error("Erro completo capturado: ", e);
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Não Autorizado", "Email ou senha inválidos", request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> handleConflict(ConflictException e, HttpServletRequest request) {
        log.error("Erro completo capturado: ", e);
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Dados", e.getMessage(), request);
    }

    private ResponseEntity<StandardError> buildErrorResponse(HttpStatus status, String error, String message, HttpServletRequest request) {
        return buildErrorResponse(status, error, message, request, null);
    }

    private ResponseEntity<StandardError> buildErrorResponse(HttpStatus status, String error, String message, HttpServletRequest request, Map<String, String> errors) {
        StandardError err = StandardError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .validationErrors(errors)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(err);
    }
}
