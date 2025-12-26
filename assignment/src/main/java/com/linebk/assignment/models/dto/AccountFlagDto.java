package com.linebk.assignment.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountFlagDto {
    private String accountId;
    private String flagType;
    private String flagValue;
}
