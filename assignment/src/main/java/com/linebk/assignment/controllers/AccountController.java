package com.linebk.assignment.controllers;

import com.linebk.assignment.models.dto.AccountDto;
import com.linebk.assignment.models.dto.SetupMainAccountRequest;
import com.linebk.assignment.models.dto.UpdateAccountDetailRequest;
import com.linebk.assignment.models.dto.WithdrawRequest;
import com.linebk.assignment.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Accounts", description = "Account management endpoints")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping()
    @Operation(
        summary = "Get user accounts",
        description = "Returns all accounts for the provided user identifier."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Accounts retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AccountDto.class))
        ),
        @ApiResponse(responseCode = "204", description = "No accounts found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<List<AccountDto>> getAccountByUserId(
        @Parameter(description = "Unique identifier of the user", example = "user-1234")
        @RequestParam("userid") String userId) {

        // Input validation
        if (!StringUtils.hasText(userId)) {
            log.warn("getAccountByUserId called with blank userId");
            return ResponseEntity.badRequest().build();
        }

        log.info("Retrieving accounts for userid={}", userId);
        List<AccountDto> accounts = accountService.getAccountByUserId(userId);

        if (accounts == null || accounts.isEmpty()) {
            log.debug("No accounts found for userid={}", userId);
            return ResponseEntity.noContent().build();
        }

        log.info("Successfully retrieved {} accounts for userid={}", accounts.size(), userId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/withdraw")
    @Operation(
        summary = "Withdraw amount from account",
        description = "Withdraw specified amount from the account. Validates account existence and sufficient balance."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters or insufficient balance"),
        @ApiResponse(responseCode = "500", description = "Account not found or internal server error")
    })
    public ResponseEntity<Void> withdrawAmount(@RequestBody WithdrawRequest request) {
        log.info("withdrawAmount called with accountId={}, amount={}", request.getAccountId(), request.getAmount());

        // Input validation
        if (!StringUtils.hasText(request.getAccountId())) {
            log.warn("withdrawAmount called with blank accountId");
            return ResponseEntity.badRequest().build();
        }

        if (request.getAmount() == null || request.getAmount().signum() <= 0) {
            log.warn("withdrawAmount called with invalid amount: {}", request.getAmount());
            return ResponseEntity.badRequest().build();
        }

        try {
            accountService.withdrawAmount(request.getAccountId(), request.getAmount());
            log.info("Withdrawal successful for accountId={}", request.getAccountId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Withdrawal failed: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/setup-main-account")
    @Operation(
        summary = "Setup main account for user",
        description = "Set the specified account as main account (is_main_account = true) and set other accounts as secondary (is_main_account = false)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Main account setup successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Account not found or internal server error")
    })
    public ResponseEntity<Void> setupMainAccount(@RequestBody SetupMainAccountRequest request) {
        log.info("setupMainAccount called with userId={}, accountId={}", request.getUserId(), request.getAccountId());

        // Input validation
        if (!StringUtils.hasText(request.getUserId())) {
            log.warn("setupMainAccount called with blank userId");
            return ResponseEntity.badRequest().build();
        }

        if (!StringUtils.hasText(request.getAccountId())) {
            log.warn("setupMainAccount called with blank accountId");
            return ResponseEntity.badRequest().build();
        }

        try {
            accountService.setupMainAccount(request.getUserId(), request.getAccountId());
            log.info("Main account setup successful for userId={}, accountId={}", request.getUserId(), request.getAccountId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Main account setup failed: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/update-detail")
    @Operation(
        summary = "Update account detail (nickname and color)",
        description = "Update nickname and color for the specified account in account_details table."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account detail updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Account not found or internal server error")
    })
    public ResponseEntity<Void> updateAccountDetail(@RequestBody UpdateAccountDetailRequest request) {
        log.info("updateAccountDetail called with accountId={}, nickname={}, color={}", request.getAccountId(), request.getNickname(), request.getColor());

        // Input validation
        if (!StringUtils.hasText(request.getAccountId())) {
            log.warn("updateAccountDetail called with blank accountId");
            return ResponseEntity.badRequest().build();
        }

        try {
            accountService.updateAccountDetail(request.getAccountId(), request.getNickname(), request.getColor());
            log.info("Account detail updated successfully for accountId={}", request.getAccountId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Account detail update failed: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }



}

