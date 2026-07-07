package com.inter.remittance.ui.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Standard error response returned on all non-2xx status codes")
public record ErrorResponse(

        @Schema(description = "ISO-8601 timestamp of when the error occurred", example = "2026-07-06T14:30:00.000Z")
        String timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "HTTP status reason phrase", example = "Bad Request")
        String error,

        @Schema(description = "Human-readable description of the error", example = "Document already exists")
        String message,

        @Schema(description = "Request URI path that triggered the error", example = "/accounts")
        String path,

        @Schema(description = "List of field-level validation errors, populated on 422/400 validation failures",
                example = "[\"name: must not be blank\", \"email: must be a well-formed email address\"]")
        List<String> details
) {}

