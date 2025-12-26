package com.linebk.assignment.services.impl;

import com.linebk.assignment.models.dto.TransactionDto;
import com.linebk.assignment.repositories.TransactionRepository;
import com.linebk.assignment.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionDto> getTransactionByUserId(String userId) {
        log.debug("TransactionServiceImpl.getTransactionByUserId userId={}", userId);
        List<TransactionDto> transactionList = transactionRepository.getTransactionByUserId(userId);
        log.debug("TransactionServiceImpl.getTransactionByUserId found {} transactions for userId={}", transactionList.size(), userId);
        return transactionList;
    }

    @Override
    public List<TransactionDto> getTransactionByUserIdWithPagination(String userId, int limit, int offset) {
        log.debug("TransactionServiceImpl.getTransactionByUserIdWithPagination userId={}, limit={}, offset={}", userId, limit, offset);
        List<TransactionDto> transactionList = transactionRepository.getTransactionByUserIdWithPagination(userId, limit, offset);
        log.debug("TransactionServiceImpl.getTransactionByUserIdWithPagination found {} transactions for userId={}", transactionList.size(), userId);
        return transactionList;
    }
}

