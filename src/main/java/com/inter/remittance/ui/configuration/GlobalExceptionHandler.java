package com.inter.remittance.ui.configuration;

import com.inter.remittance.domain.commom.Exceptions.BusinessException;
import com.inter.remittance.ui.responses.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestClientException.class)
    ResponseEntity<Object> handleRestClient(RestClientException ex, WebRequest request) {
        ErrorResponse body = buildError(HttpStatus.BAD_GATEWAY,
                "An error occurred while communicating with an external service. Please try again later.",
                request, List.of());
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse body = buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request, List.of());
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<Object> handleConflict(IllegalStateException ex, WebRequest request) {
        ErrorResponse body = buildError(HttpStatus.CONFLICT, ex.getMessage(), request, List.of());
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<Object> handleBusiness(BusinessException ex, WebRequest request) {
        ErrorResponse body = buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request, List.of());
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    ResponseEntity<Object> handleDataIntegrity(
            org.springframework.dao.DataIntegrityViolationException ex,
            WebRequest request
    ) {
        ErrorResponse body = buildError(HttpStatus.CONFLICT,
                "The request conflicts with existing data. A unique or foreign key constraint was violated.",
                request, List.of());
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ErrorResponse body = buildError(HttpStatus.BAD_REQUEST,
                "Request validation failed. Check the 'details' field for each invalid field.",
                request, details);
        return super.handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ErrorResponse body = buildError(HttpStatus.BAD_REQUEST,
                "The request body is malformed or contains invalid JSON. Please review the payload and try again.",
                request, List.of());
        return super.handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleUnexpected(Exception ex, WebRequest request) {
        ErrorResponse body = buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal error occurred. Please contact support if the problem persists.",
                request,
                List.of()
        );
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ErrorResponse buildError(HttpStatus status, String message, WebRequest request, List<String> details) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ErrorResponse(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details
        );
    }
}
