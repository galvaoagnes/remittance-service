package com.inter.remittance.ui.controller;

import com.inter.remittance.application.service.AuthService;
import com.inter.remittance.ui.requests.LoginRequest;
import com.inter.remittance.ui.responses.ErrorResponse;
import com.inter.remittance.ui.responses.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Obtain a JWT token to authenticate subsequent requests")
@Profile("api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate and obtain a JWT token",
            description = """
                    Validates the provided document number and password.
                    On success, returns a Bearer JWT token valid for subsequent authenticated requests.
                    Include the token as `Authorization: Bearer <token>` on all protected endpoints.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful — JWT token returned",
                            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Request body is malformed or required fields are missing",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials — document or password is incorrect",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<TokenResponse> login(@Valid  @RequestBody LoginRequest request) {
        String token = authService.login(request.document(), request.password());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
