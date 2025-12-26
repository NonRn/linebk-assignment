package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    List<TransactionDto> getTransactionByUserId(String userId);

    List<TransactionDto> getTransactionByUserIdWithPagination(String userId, int limit, int offset);
}

