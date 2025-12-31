package com.linebk.assignment.controllers;

import com.linebk.assignment.models.dto.TransactionDto;
import com.linebk.assignment.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/transaction")
@Tag(name = "Transactions", description = "Transaction management endpoints")
public class TransactionController {

    private static final int DEFAULT_LIMIT = 10;
    private static final int DEFAULT_OFFSET = 0;
    private static final int MAX_LIMIT = 1000;

    @Autowired
    private TransactionService transactionService;

    @GetMapping()
    @Operation(
        summary = "Get user transactions with pagination",
        description = "Returns transactions for the provided user identifier with limit and offset pagination."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Transactions retrieved successfully",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TransactionDto.class)))
        ),
        @ApiResponse(responseCode = "204", description = "No transactions found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TransactionDto>> getTransactionByUserIdWithPagination(
        @Parameter(description = "Unique identifier of the user", example = "user-1234", required = true)
        @RequestParam("userid") String userId,
        @Parameter(description = "Number of records to retrieve", example = "10")
        @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT + "") int limit,
        @Parameter(description = "Number of records to skip", example = "0")
        @RequestParam(value = "offset", defaultValue = DEFAULT_OFFSET + "") int offset) {

        // Input validation
        if (!StringUtils.hasText(userId)) {
            log.warn("getTransactionByUserIdWithPagination called with blank userId");
            return ResponseEntity.badRequest().build();
        }

        if (offset < 0) {
            log.warn("getTransactionByUserIdWithPagination called with negative offset={}, using default", offset);
            offset = DEFAULT_OFFSET;
        }

        if (limit <= 0 || limit > MAX_LIMIT) {
            log.warn("getTransactionByUserIdWithPagination called with invalid limit={}, constraining to default range", limit);
            limit = DEFAULT_LIMIT;
        }

        try {
            log.info("Retrieving transactions for userid={}, limit={}, offset={}", userId, limit, offset);
            List<TransactionDto> transactions = transactionService.getTransactionByUserIdWithPagination(userId, limit, offset);

            if (transactions == null || transactions.isEmpty()) {
                log.debug("No transactions found for userid={}", userId);
                return ResponseEntity.noContent().build();
            }

            log.info("Successfully retrieved {} transactions for userid={}", transactions.size(), userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error retrieving transactions for userid={}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

