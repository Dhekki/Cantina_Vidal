package org.senai.cantina_vidal.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

        log.warn("Erros de validação do cliente: {}", errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de Validação", "Um ou mais campos estão inválidos", request, errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleEntityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        log.warn("Recurso não encontrado: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", e.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        log.warn("Falha de autenticação: Usuário forneceu credenciais inválidas.");
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Não Autorizado", "Email ou senha inválidos", request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> handleConflict(ConflictException e, HttpServletRequest request) {
        log.warn("Conflito de regra de negócio: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Dados", e.getMessage(), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<StandardError> handleMaxSizeException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.warn("Upload negado: Tamanho máximo excedido.");
        return buildErrorResponse(HttpStatus.CONTENT_TOO_LARGE, "Arquivo muito grande", "O arquivo excede o limite máximo permitido.", request);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<StandardError> handleFileStorageException(FileStorageException e, HttpServletRequest request) {
        log.error("Erro crítico no sistema de arquivos: ", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro no Servidor", "Falha interna ao processar o arquivo.", request);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<StandardError> handleInvalidFileException(InvalidFileException e, HttpServletRequest request) {
        log.warn("Arquivo inválido rejeitado: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Arquivo Inválido", e.getMessage(), request);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<StandardError> handleInvalidTokenException(InvalidTokenException e, HttpServletRequest request) {
        log.warn("Acesso negado: Token inválido ou expirado.");
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Token Inválido", e.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        log.debug("Detalhes do erro SQL: ", e.getMostSpecificCause());
        log.warn("Erro de violação de dados (SQL). Uma constraint de banco foi acionada.");
        String safeMessage = "Não foi possível concluir a operação devido a um conflito de dados (ex: registro duplicado ou em uso).";
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflito de Dados", safeMessage, request);
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
