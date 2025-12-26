package com.linebk.assignment.controllers;

import com.linebk.assignment.models.dto.AccountDto;
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
}

