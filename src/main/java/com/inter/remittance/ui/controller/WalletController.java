package com.inter.remittance.ui.controller;

import com.inter.remittance.application.service.CreateWalletService;
import com.inter.remittance.application.service.FindTransactionService;
import com.inter.remittance.ui.mappers.PageResultResponseMapper;
import com.inter.remittance.ui.mappers.TransactionResponseMapper;
import com.inter.remittance.ui.mappers.WalletResponseMapper;
import com.inter.remittance.ui.requests.CreateWalletRequest;
import com.inter.remittance.ui.responses.ErrorResponse;
import com.inter.remittance.ui.responses.PageResultResponse;
import com.inter.remittance.ui.responses.TransactionResponse;
import com.inter.remittance.ui.responses.WalletCreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@Tag(name = "Wallets", description = "Standalone wallet creation endpoint")
@SecurityRequirement(name = "bearerAuth")
@Profile("api")
public class WalletController {
    private final CreateWalletService createWalletService;
    private final FindTransactionService findTransactionService;

    public WalletController(CreateWalletService createWalletService, FindTransactionService findTransactionService) {
        this.createWalletService = createWalletService;
        this.findTransactionService = findTransactionService;
    }

    @PostMapping("{accountId}")
    @Operation(
            summary = "Create a new wallet for an account",
            description = "Adds a wallet with zero balance for the specified currency to the given account. Each account may have at most one wallet per currency.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Wallet created successfully",
                            content = @Content(schema = @Schema(implementation = WalletCreatedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Request body is malformed or contains invalid JSON",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found for the given ID",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Business rule violation — a wallet for this currency already exists on the account",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<WalletCreatedResponse> deposit(
            @Parameter(description = "UUID of the account to create the wallet for", required = true)
            @PathVariable UUID accountId,
            @Valid  @RequestBody CreateWalletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                WalletResponseMapper.toResponse(
                        createWalletService.create(accountId, request.currency())
                )
        );
    }

    @Operation(
            summary = "List wallet transactions (paginated)",
            description = """
                Returns a paginated list of transactions for the given wallet.
                The result includes transactions where the wallet is source or destination,
                depending on your service/repository implementation.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transactions returned successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid pagination parameters or malformed request",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Wallet not found for the given ID",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("{id}/transactions")
    ResponseEntity<PageResultResponse<TransactionResponse>> getWalletTransactions(
            @PathVariable UUID id,
            @Parameter(description = "Zero-based page index", example = "0") @RequestParam int page,
            @Parameter(description = "Number of accounts per page", example = "10") @RequestParam int size
     ){
       return ResponseEntity.ok(PageResultResponseMapper.toResponse(
                findTransactionService.findAllTransactions(id, page, size),
                TransactionResponseMapper::toResponse
        ));
    }
}
