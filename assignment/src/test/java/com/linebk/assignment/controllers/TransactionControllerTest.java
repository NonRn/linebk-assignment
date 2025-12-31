package com.linebk.assignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linebk.assignment.models.dto.TransactionDto;
import com.linebk.assignment.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        TransactionService transactionService() {
            return mock(TransactionService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionService transactionService;

    private TransactionDto testTransaction;
    private TransactionDto testTransaction2;
    private String testUserId;
    private int testLimit;
    private int testOffset;

    @BeforeEach
    void setUp() {
        reset(transactionService);

        testUserId = "000018b0e1a211ef95a30242ac180002";
        testLimit = 5;
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

    // ------------------ getTransactionByUserIdWithPagination ------------------
    @Test
    void getTransactionByUserIdWithPagination_success() throws Exception {
        when(transactionService.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(get("/api/v1/transaction")
                        .param("userid", testUserId)
                        .param("limit", String.valueOf(testLimit))
                        .param("offset", String.valueOf(testOffset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].userId", is(testUserId)));
    }

    @Test
    void getTransactionByUserIdWithPagination_multipleTransactions() throws Exception {
        when(transactionService.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset))
                .thenReturn(List.of(testTransaction, testTransaction2));

        mockMvc.perform(get("/api/v1/transaction")
                        .param("userid", testUserId)
                        .param("limit", String.valueOf(testLimit))
                        .param("offset", String.valueOf(testOffset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(transactionService, times(1)).getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset);
    }

    @Test
    void getTransactionByUserIdWithPagination_noContent() throws Exception {
        when(transactionService.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/transaction")
                        .param("userid", testUserId)
                        .param("limit", String.valueOf(testLimit))
                        .param("offset", String.valueOf(testOffset)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTransactionByUserIdWithPagination_badRequest_blankUserId() throws Exception {
        mockMvc.perform(get("/api/v1/transaction")
                        .param("userid", "")
                        .param("limit", String.valueOf(testLimit))
                        .param("offset", String.valueOf(testOffset)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).getTransactionByUserIdWithPagination(any(), anyInt(), anyInt());
    }

    @Test
    void getTransactionByUserIdWithPagination_withDefaultPagination() throws Exception {
        when(transactionService.getTransactionByUserIdWithPagination(testUserId, 10, 0))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(get("/api/v1/transaction")
                        .param("userid", testUserId))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).getTransactionByUserIdWithPagination(testUserId, 10, 0);
    }

    @Test
    void getTransactionByUserIdWithPagination_invalidLimitOffset() throws Exception {
        when(transactionService.getTransactionByUserIdWithPagination(testUserId, 10, 0))
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(get("/api/v1/transaction")
                        .param("userid", testUserId)
                        .param("limit", "-5")
                        .param("offset", "-10"))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).getTransactionByUserIdWithPagination(testUserId, 10, 0);
    }

    @Test
    void getTransactionByUserIdWithPagination_internalServerError() throws Exception {
        when(transactionService.getTransactionByUserIdWithPagination(testUserId, testLimit, testOffset))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/transaction")
                        .param("userid", testUserId)
                        .param("limit", String.valueOf(testLimit))
                        .param("offset", String.valueOf(testOffset)))
                .andExpect(status().isInternalServerError());
    }
}


