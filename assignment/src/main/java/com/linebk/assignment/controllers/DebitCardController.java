package com.linebk.assignment.controllers;

import com.linebk.assignment.models.dto.DebitCardDto;
import com.linebk.assignment.services.DebitCardService;
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
@RequestMapping("/api/v1/debitcard")
@Tag(name = "Debit Cards", description = "Debit card management endpoints")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @GetMapping()
    @Operation(
        summary = "Get user debit cards",
        description = "Returns all debit cards for the provided user identifier."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Debit cards retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = DebitCardDto.class))
        ),
        @ApiResponse(responseCode = "204", description = "No debit cards found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DebitCardDto>> getDebitCardByUserId(
        @Parameter(description = "Unique identifier of the user", example = "user-1234")
        @RequestParam("userid") String userId) {

        // Input validation
        if (!StringUtils.hasText(userId)) {
            log.warn("getDebitCardByUserId called with blank userId");
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("Retrieving debit cards for userid={}", userId);
            List<DebitCardDto> debitCards = debitCardService.getDebitCardByUserId(userId);

            if (debitCards == null || debitCards.isEmpty()) {
                log.debug("No debit cards found for userid={}", userId);
                return ResponseEntity.noContent().build();
            }

            log.info("Successfully retrieved {} debit cards for userid={}", debitCards.size(), userId);
            return ResponseEntity.ok(debitCards);
        } catch (Exception e) {
            log.error("Error retrieving debit cards for userid={}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

