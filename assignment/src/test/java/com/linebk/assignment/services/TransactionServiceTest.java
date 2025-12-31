package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.TransactionDto;
import com.linebk.assignment.repositories.TransactionRepository;
import com.linebk.assignment.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionDto testTransaction;
    private TransactionDto testTransaction2;
    private String testUserId;
    private int testLimit;
    private int testOffset;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testUserId = "000018b0e1a211ef95a30242ac180002";
        testLimit = 10;
        testOffset = 0;

        testTransaction = new TransactionDto();
        testTransaction.setUserId(testUserId);
        testTransaction.setName("Transaction_133565");
        testTransaction.setImage("https://dummyimage.com/54x54/999/fff");
        testTransaction.setIsBank(false);

        testTransaction2 = new TransactionDto();
        testTransaction2.setUserId(testUserId);
        testTransaction2.setName("Transaction_133566");
        testTransaction2.setImage("https://dummyimage.com/54x54/999/fff");
        testTransaction2.setIsBank(true);
    }

    /* ==================== Test GetTransactionByUserId ==================== */
    @Test
    void testGetTransactionByUserId_Success() {
        // Arrange
        List<TransactionDto> transactionList = new ArrayList<>();
        transactionList.add(testTransaction);
        transactionList.add(testTransaction2);

        when(transactionRepository.getTransactionByUserId(testUserId)).thenReturn(transactionList);

        // Act
        List<TransactionDto> result = transactionService.getTransactionByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionList.size(), result.size());
    }

    @Test
    void testGetTransactionByUserId_EmptyResult() {
        // Arrange
        when(transactionRepository.getTransactionByUserId(testUserId)).thenReturn(new ArrayList<>());

        // Act
        List<TransactionDto> result = transactionService.getTransactionByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /* ==================== Test GetTransactionByUserIdWithPagination ==================== */
    @Test
    void testGetTransactionByUserIdWithPagination_Success() {
        // Arrange
        List<TransactionDto> transactionList = new ArrayList<>();
        transactionList.add(testTransaction);
        transactionList.add(testTransaction2);

        when(transactionRepository.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset))
            .thenReturn(transactionList);

        // Act
        List<TransactionDto> result = transactionService.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset);

        // Assert
        assertNotNull(result);
        assertEquals(transactionList.size(), result.size());
    }

    @Test
    void testGetTransactionByUserIdWithPagination_EmptyResult() {
        // Arrange
        when(transactionRepository.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset))
            .thenReturn(new ArrayList<>());

        // Act
        List<TransactionDto> result = transactionService.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

