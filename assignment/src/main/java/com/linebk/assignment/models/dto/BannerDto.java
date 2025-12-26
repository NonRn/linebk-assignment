package com.linebk.assignment.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDto {

    private String userId;
    private String title;
    private String description;
    private String image;
    private String linkUrl;

}
