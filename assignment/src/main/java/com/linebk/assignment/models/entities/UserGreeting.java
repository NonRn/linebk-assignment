package com.linebk.assignment.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "user_greetings")
public class UserGreeting {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "greeting")
    private String greeting;

    @Column(name = "dummy_col_2", length = 255)
    private String dummyCol2;
}
