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
@Table(name = "debit_cards")
public class DebitCard {

    @Id
    @Column(name = "card_id", length = 50)
    private String cardId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "dummy_col_7", length = 255)
    private String dummyCol7;
}
