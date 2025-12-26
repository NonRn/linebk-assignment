package com.linebk.assignment.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "banners")
public class Banner {

    @Id
    @Column(name = "banner_id", length = 50)
    private String bannerId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "dummy_col_11", length = 255)
    private String dummyCol11;

    @Column(name = "link_url", length = 255)
    private String linkUrl;
}
