package com.linebk.assignment.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String userId;
    private String name;
    private String image;
    private Boolean isBank;
}
