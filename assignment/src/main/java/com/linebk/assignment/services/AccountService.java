package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.AccountDto;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<AccountDto> getAccountByUserId(String userId);

    int withdrawAmount(String accountId, BigDecimal amount);

    int setupMainAccount(String userId, String accountId);

    int updateAccountDetail(String accountId, String nickname, String color);
}

