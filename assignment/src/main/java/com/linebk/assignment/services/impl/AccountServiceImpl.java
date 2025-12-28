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

import java.math.BigDecimal;
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

    @Override
    public void withdrawAmount(String accountId, BigDecimal amount) {
        log.info("AccountServiceImpl.withdrawAmount accountId={}, amount={}", accountId, amount);

        // Check if account exists
        if (!accountRepository.isAccountExists(accountId)) {
            log.error("Account not found: accountId={}", accountId);
            throw new RuntimeException("Account not found: " + accountId);
        }

        // Get current balance
        BigDecimal currentBalance = accountRepository.getAccountBalance(accountId);
        if (currentBalance == null) {
            log.error("Failed to retrieve balance for accountId={}", accountId);
            throw new RuntimeException("Failed to retrieve account balance");
        }

        log.debug("Current balance for accountId={}: {}", accountId, currentBalance);

        // Check if balance is sufficient
        if (currentBalance.compareTo(amount) < 0) {
            log.error("Insufficient balance for accountId={}, current={}, requested={}", accountId, currentBalance, amount);
            throw new RuntimeException("Insufficient balance. Current: " + currentBalance + ", Requested: " + amount);
        }

        // Update balance
        BigDecimal newBalance = currentBalance.subtract(amount);
        accountRepository.updateAccountBalance(accountId, newBalance);

        log.info("Withdraw successful for accountId={}, oldBalance={}, newBalance={}", accountId, currentBalance, newBalance);
    }

    @Override
    public void setupMainAccount(String userId, String accountId) {
        log.info("AccountServiceImpl.setupMainAccount userId={}, accountId={}", userId, accountId);

        // Check if account exists
        if (!accountRepository.isAccountExists(accountId)) {
            log.error("Account not found: accountId={}", accountId);
            throw new RuntimeException("Account not found: " + accountId);
        }

        accountRepository.setupMainAccount(userId, accountId);
        log.info("Main account setup successfully for userId={}, accountId={}", userId, accountId);
    }

    @Override
    public void updateAccountDetail(String accountId, String nickname, String color) {
        log.info("AccountServiceImpl.updateAccountDetail accountId={}, nickname={}, color={}", accountId, nickname, color);

        // Check if account exists
        if (!accountRepository.isAccountExists(accountId)) {
            log.error("Account not found: accountId={}", accountId);
            throw new RuntimeException("Account not found: " + accountId);
        }

        accountRepository.updateAccountDetail(accountId, nickname, color);
        log.info("Account detail updated successfully for accountId={}", accountId);
    }
}