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
@Table(name = "debit_card_status")
public class DebitCardStatus {

    @Id
    @Column(name = "card_id", length = 50)
    private String cardId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "dummy_col_8", length = 255)
    private String dummyCol8;
}
