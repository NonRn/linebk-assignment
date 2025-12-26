package com.linebk.assignment.models.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String accountId;
    private String userId;
    private String type;
    private String currency;
    private String accountNumber;
    private String issuer;
    private BigDecimal amount;

    private String nickname;
    private String color;
    private Boolean isMainAccount;
    private Integer progress;

    private List<AccountFlagDto> flags;
}
