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
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id", length = 50)
    private String transactionId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "is_bank")
    private Boolean bank;

    @Column(name = "dummy_col_6", length = 255)
    private String dummyCol6;
}
