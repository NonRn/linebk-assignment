package com.linebk.assignment.services.impl;

import com.linebk.assignment.models.dto.AccountDto;
import com.linebk.assignment.models.dto.AccountFlagDto;
import com.linebk.assignment.repositories.AccountRepository;
import com.linebk.assignment.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDto> getAccountByUserId(String userId) {
        log.info("AccountServiceImpl.getAccountByUserId userId={}", userId);
        List<AccountDto> accountList = accountRepository.getAccountByUserId(userId);

        if(!CollectionUtils.isEmpty(accountList)) {
            log.info("AccountServiceImpl.getAccountByUserId account size={}, userId={}", accountList.size(), userId);
            List<String> accountIdList = accountList.stream().map(AccountDto::getAccountId).toList();
            List<AccountFlagDto> flagList = accountRepository.getAccountFlagByAccountIdList(accountIdList);
            for (AccountDto account : accountList) {
                List<AccountFlagDto> filteredFlags = flagList.stream().filter(flag -> StringUtils.equals(flag.getAccountId(), account.getAccountId())).toList();
                account.setFlags(filteredFlags);
            }
        } else {
            log.info("AccountServiceImpl.getAccountByUserId account is empty for userId={}", userId);
        }

        return accountList;
    }
}