package com.linebk.assignment.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userId;
    private String name;
    private String profileImage;
    private String greetingText;
}
