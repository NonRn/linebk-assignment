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
@Table(name = "account_details")
public class AccountDetail {

    @Id
    @Column(name = "account_id", length = 50)
    private String accountId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "color", length = 10)
    private String color;

    @Column(name = "is_main_account")
    private Boolean isMainAccount;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "dummy_col_5", length = 255)
    private String dummyCol5;

    @Column(name = "nickname", length = 100)
    private String nickname;
}
