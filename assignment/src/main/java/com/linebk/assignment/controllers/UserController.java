package com.linebk.assignment.controllers;

import com.linebk.assignment.models.dto.UserDto;
import com.linebk.assignment.models.dto.AuthRequest;
import com.linebk.assignment.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "Users", description = "User profile and authentication endpoints")
public class UserController {

    @Autowired
	private UserService userService;

	@GetMapping()
    @Operation(
        summary = "Get user details",
        description = "Returns the profile data for the provided user identifier."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User profile retrieved",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class))
        ),
        @ApiResponse(responseCode = "204", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
	public ResponseEntity<UserDto> getUserById(
        @Parameter(description = "Unique identifier of the user", example = "user-1234")
        @RequestParam("userid") String userId) {
        log.info("Received request to get user with id={}", userId);
		UserDto user = userService.getUserById(userId);
        if(user == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(user);
	}

    @PostMapping("/auth/passcode")
    @Operation(
        summary = "Authenticate user by passcode",
        description = "Validates the passcode for the user and returns the authentication status."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "401", description = "Authentication failed"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<String> authenticatePasscodeUser(@Valid @RequestBody AuthRequest authRequest) {
        String userId = authRequest.getUserid();
        String passcode = authRequest.getPasscode();
        log.info("Authenticating user with id={}", userId);
        
        UserDto user = userService.getUserById(userId);
        boolean authenticated = StringUtils.equals(passcode, passcode); // Dummy authentication logic = true
        if (user != null && authenticated) { //TODO implement real authentication
            log.info("User {} authenticated successfully", userId);
            return ResponseEntity.ok("Authentication successful");
        } else {
            log.warn("Authentication failed for user {}", userId);
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "Authenticate user by username and password",
            description = "Validates the User ID and Password for the user and returns the authentication status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Authentication failed"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<String> authenticateLoginUser(@Valid @RequestBody AuthRequest authRequest) {
        String userId = authRequest.getUserid();
        String passcode = authRequest.getPasscode();
        log.info("Authenticating user with id={}", userId);

        UserDto user = userService.getUserById(userId);
        boolean authenticated = StringUtils.equals(passcode, passcode); // Dummy authentication logic = true
        if (user != null && authenticated) { //TODO implement real authentication
            log.info("User {} authenticated successfully", userId);
            return ResponseEntity.ok("Authentication successful");
        } else {
            log.warn("Authentication failed for user {}", userId);
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }

}
