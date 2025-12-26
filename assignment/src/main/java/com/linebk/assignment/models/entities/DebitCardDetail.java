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
@Table(name = "debit_card_details")
public class DebitCardDetail {

    @Id
    @Column(name = "card_id", length = 50)
    private String cardId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "issuer", length = 100)
    private String issuer;

    @Column(name = "number", length = 25)
    private String number;

    @Column(name = "dummy_col_10", length = 255)
    private String dummyCol10;
}
