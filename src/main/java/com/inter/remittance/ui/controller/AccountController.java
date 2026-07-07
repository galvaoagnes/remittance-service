package com.inter.remittance.ui.controller;

import com.inter.remittance.application.command.CreateDepositCommand;
import com.inter.remittance.application.command.CreateWithdrawalCommand;
import com.inter.remittance.application.service.*;
import com.inter.remittance.ui.mappers.*;
import com.inter.remittance.ui.requests.BalanceUpdateRequest;
import com.inter.remittance.ui.requests.CreateAccountRequest;
import com.inter.remittance.ui.requests.CreateRemittanceRequest;
import com.inter.remittance.ui.requests.CreateWalletRequest;
import com.inter.remittance.ui.responses.*;
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
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Account lifecycle: creation, lookup, status management, wallets, deposits, withdrawals and remittances")
@SecurityRequirement(name = "bearerAuth")
@Profile("api")
public class AccountController {

    private final CreateAccountService createAccountService;
    private final FindAccountService findAccountService;
    private final UpdateAccountService updateAccountService;
    private final CreateWalletService createWalletService;
    private final CreateDepositService createDepositService;
    private final CreateWithdrawalService createWithdrawalService;
    private final CreateRemittanceService createRemittanceService;

    public AccountController(CreateAccountService createAccountService, FindAccountService findAccountService, UpdateAccountService updateAccountService, CreateWalletService createWalletService, CreateDepositService createDepositService, CreateWithdrawalService createWithdrawalService, CreateRemittanceService createRemittanceService) {
        this.createAccountService = createAccountService;
        this.findAccountService = findAccountService;
        this.updateAccountService = updateAccountService;
        this.createWalletService = createWalletService;
        this.createDepositService = createDepositService;
        this.createWithdrawalService = createWithdrawalService;
        this.createRemittanceService = createRemittanceService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new account",
            description = "Registers a new person and their associated account with default wallets for each supported currency. The document number must be unique.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Account created successfully",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Request body is malformed or contains invalid JSON",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "A person with the given document number already exists",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Business rule violation (e.g. invalid CPF/CNPJ format, invalid email or password pattern)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AccountResponseMapper.toResponse(
                        createAccountService.execute(AccountRequestMapper.toCommand(request))
                ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find account by ID",
            description = "Returns full account details including the associated person and all wallets.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account found",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found for the given ID",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<AccountResponse> findById(
            @Parameter(description = "UUID of the account", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(AccountResponseMapper.toResponse(findAccountService.findAccountById(id)));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update account active status",
            description = "Activates or deactivates an account. A deactivated account cannot perform transactions.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Status updated successfully — no body returned"),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found for the given ID",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<Void> setActive(
            @Parameter(description = "UUID of the account", required = true) @PathVariable UUID id,
            @Parameter(description = "New active status", required = true) @RequestParam boolean isActive) {
        updateAccountService.updateActiveStatuses(id, isActive);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/wallets/deposit")
    @Operation(
            summary = "Deposit into account wallet",
            description = "Credits the specified amount to the wallet matching the given currency. The wallet must already exist for that currency.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deposit completed — transaction details returned",
                            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid amount or malformed payload",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account or wallet not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Business rule violation (e.g. wallet not found for the given currency)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<TransactionResponse> deposit(
            @Parameter(description = "UUID of the account", required = true) @PathVariable UUID id,
            @Valid @RequestBody BalanceUpdateRequest request) {
        return ResponseEntity.ok(TransactionResponseMapper.toResponse(
                createDepositService.process(new CreateDepositCommand(id, request.currency(), request.amount()))
        ));
    }

    @PostMapping("{id}/withdrawal")
    @Operation(
            summary = "Withdraw from account wallet",
            description = "Debits the specified amount from the wallet matching the given currency. The wallet must have sufficient balance.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Withdrawal completed — transaction details returned",
                            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid amount or malformed payload",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account or wallet not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Business rule violation (e.g. insufficient funds, wallet not found)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<TransactionResponse> withdrawal(
            @Parameter(description = "UUID of the account", required = true) @PathVariable UUID id,
            @Valid @RequestBody BalanceUpdateRequest request) {
        return ResponseEntity.ok(TransactionResponseMapper.toResponse(
                createWithdrawalService.process(new CreateWithdrawalCommand(id, request.currency(), request.amount()))
        ));
    }

    @PostMapping("/remittance")
    @Operation(
            summary = "Create international remittance",
            description = """
                    Transfers funds from the source account's BRL wallet to the destination account's wallet
                    in the requested currency, applying the current exchange rate.
                    
                    The source account must have:
                    - Sufficient balance in the BRL wallet.
                    - Remaining daily transaction limit for the transfer amount.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Remittance processed — remittance and transaction details returned",
                            content = @Content(schema = @Schema(implementation = RemittanceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed or invalid payload",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Source or destination account not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Business rule violation (e.g. insufficient funds, daily limit exceeded, wallet not found for currency)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "502", description = "Failed to retrieve exchange rate from external service",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    ResponseEntity<RemittanceResponse> remittance(@RequestBody CreateRemittanceRequest request) {
        return ResponseEntity.ok(RemittanceResponseMapper.toResponse(
                createRemittanceService.process(RemittanceRequestMapper.toCommand(request))
        ));
    }

    @PostMapping("{id}/wallets")
    @Operation(
            summary = "Create a new wallet for an account",
            description = "Adds a new wallet with zero balance for the specified currency. Each account can have at most one wallet per currency.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Wallet created successfully",
                            content = @Content(schema = @Schema(implementation = WalletCreatedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Malformed or invalid payload",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid Bearer token",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found for the given ID",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "422", description = "Business rule violation (e.g. wallet for this currency already exists)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<WalletCreatedResponse> createWallet(
            @Parameter(description = "UUID of the account", required = true) @PathVariable UUID id,
            @Valid @RequestBody CreateWalletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                WalletResponseMapper.toResponse(createWalletService.create(id, request.currency()))
        );
    }
}
