package com.linebk.assignment.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountDetailRequest {

    private String accountId;
    private String nickname;
    private String color;
}

