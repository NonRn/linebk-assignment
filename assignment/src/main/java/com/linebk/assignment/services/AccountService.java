package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.AccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAccountByUserId(String userId);
}

