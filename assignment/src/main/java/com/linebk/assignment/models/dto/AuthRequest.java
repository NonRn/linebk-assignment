package com.linebk.assignment.models.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Request body for passcode authentication")
public class AuthRequest {
    @Schema(description = "Unique identifier of the user", example = "user-1234")
    private String userid;

    @Schema(description = "User passcode", example = "123456")
    private String passcode;
}

