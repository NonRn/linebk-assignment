package com.linebk.assignment.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebitCardDto {

    private String cardId;
    private String userId;
    private String name;
    private String issuer;
    private String number;
    private String status;

    /* design */
    private String color;
    private String borderColor;

}
