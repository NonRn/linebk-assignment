package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.AccountDto;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<AccountDto> getAccountByUserId(String userId);

    void withdrawAmount(String accountId, BigDecimal amount);

    void setupMainAccount(String userId, String accountId);

    void updateAccountDetail(String accountId, String nickname, String color);
}

