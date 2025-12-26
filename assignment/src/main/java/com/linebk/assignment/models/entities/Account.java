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
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_id", length = 50)
    private String accountId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "issuer", length = 100)
    private String issuer;

    @Column(name = "dummy_col_3", length = 255)
    private String dummyCol3;
}
