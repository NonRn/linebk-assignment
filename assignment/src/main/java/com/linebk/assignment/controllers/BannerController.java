package com.linebk.assignment.controllers;

import com.linebk.assignment.models.dto.BannerDto;
import com.linebk.assignment.services.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/banner")
@Tag(name = "Banners", description = "Banner management endpoints")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping()
    @Operation(
        summary = "Get user banners",
        description = "Returns all banners for the provided user identifier."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Banners retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BannerDto.class))
        ),
        @ApiResponse(responseCode = "204", description = "No banners found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<List<BannerDto>> getBannerByUserId(
        @Parameter(description = "Unique identifier of the user", example = "user-1234")
        @RequestParam("userid") String userId) {

        // Input validation
        if (!StringUtils.hasText(userId)) {
            log.warn("getBannerByUserId called with blank userId");
            return ResponseEntity.badRequest().build();
        }

        log.info("Retrieving banners for userid={}", userId);
        List<BannerDto> banners = bannerService.getBannerByUserId(userId);

        if (banners == null || banners.isEmpty()) {
            log.debug("No banners found for userid={}", userId);
            return ResponseEntity.noContent().build();
        }

        log.info("Successfully retrieved {} banners for userid={}", banners.size(), userId);
        return ResponseEntity.ok(banners);
    }
}

