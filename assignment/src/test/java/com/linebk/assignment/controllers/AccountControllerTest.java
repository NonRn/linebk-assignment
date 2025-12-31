package com.linebk.assignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linebk.assignment.models.dto.AccountDto;
import com.linebk.assignment.models.dto.SetupMainAccountRequest;
import com.linebk.assignment.models.dto.UpdateAccountDetailRequest;
import com.linebk.assignment.models.dto.WithdrawRequest;
import com.linebk.assignment.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        AccountService accountService() {
            return mock(AccountService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountService accountService;

    private AccountDto testAccount;
    private WithdrawRequest testWithdrawRequest;
    private SetupMainAccountRequest testSetupRequest;
    private UpdateAccountDetailRequest testUpdateRequest;
    private String testUserId;
    private String testAccountId;

    @BeforeEach
    void setUp() {
        reset(accountService);

        testUserId = "000018b0e1a211ef95a30242ac180002";
        testAccountId = "000028aae1a211ef95a30242ac180002";


        testAccount = AccountDto.builder()
                .accountId(testAccountId)
                .userId(testUserId)
                .accountNumber("568-2-45295")
                .issuer("TestLab")
                .amount(new BigDecimal("1000.00"))
                .build();

        testWithdrawRequest = WithdrawRequest.builder()
                .accountId(testAccountId)
                .amount(new BigDecimal("200.00"))
                .build();

        testSetupRequest = new SetupMainAccountRequest();
        testSetupRequest.setUserId(testUserId);
        testSetupRequest.setAccountId(testAccountId);

        testUpdateRequest = new UpdateAccountDetailRequest();
        testUpdateRequest.setAccountId(testAccountId);
        testUpdateRequest.setNickname("My Account");
        testUpdateRequest.setColor("#FF5733");
    }

    // ------------------ getAccountByUserId ------------------
    @Test
    void getAccountByUserId_success() throws Exception {
        when(accountService.getAccountByUserId(testUserId)).thenReturn(List.of(testAccount));

        mockMvc.perform(get("/api/v1/account").param("userid", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].userId", is(testUserId)));
    }

    @Test
    void getAccountByUserId_noContent() throws Exception {
        when(accountService.getAccountByUserId(testUserId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/account").param("userid", testUserId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAccountByUserId_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/account").param("userid", ""))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).getAccountByUserId(any());
    }

    @Test
    void getAccountByUserId_serviceThrows() throws Exception {
        when(accountService.getAccountByUserId(testUserId)).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(get("/api/v1/account").param("userid", testUserId))
                .andExpect(status().isInternalServerError());
    }

    // ------------------ withdrawAmount ------------------
    @Test
    void withdrawAmount_success() throws Exception {
        when(accountService.withdrawAmount(eq(testAccountId), any(BigDecimal.class))).thenReturn(1);

        mockMvc.perform(post("/api/v1/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testWithdrawRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void withdrawAmount_noContent() throws Exception {
        WithdrawRequest req = WithdrawRequest.builder()
                .accountId(testAccountId)
                .amount(new BigDecimal("50.00"))
                .build();

        when(accountService.withdrawAmount(eq(testAccountId), any(BigDecimal.class))).thenReturn(0);

        mockMvc.perform(post("/api/v1/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());
    }

    @Test
    void withdrawAmount_badRequest_noParam() throws Exception {
        mockMvc.perform(post("/api/v1/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).withdrawAmount(any(), any());
    }

    @Test
    void withdrawAmount_badRequest_invalidAmount() throws Exception {
        WithdrawRequest req = WithdrawRequest.builder()
                .accountId(testAccountId)
                .amount(BigDecimal.valueOf(-50))
                .build();

        mockMvc.perform(post("/api/v1/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).withdrawAmount(any(), any());
    }

    @Test
    void withdrawAmount_badRequest_blankAccountId() throws Exception {
        WithdrawRequest req = WithdrawRequest.builder()
                .amount(BigDecimal.valueOf(200))
                .build();

        mockMvc.perform(post("/api/v1/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).withdrawAmount(any(), any());
    }

    @Test
    void withdrawAmount_serviceThrows() throws Exception {
        when(accountService.withdrawAmount(eq(testAccountId), any(BigDecimal.class))).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(post("/api/v1/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testWithdrawRequest)))
                .andExpect(status().isInternalServerError());
    }

    // ------------------ setupMainAccount ------------------
    @Test
    void setupMainAccount_success() throws Exception {
        when(accountService.setupMainAccount(testSetupRequest.getUserId(), testSetupRequest.getAccountId())).thenReturn(1);

        mockMvc.perform(post("/api/v1/account/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSetupRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void setupMainAccount_noContent() throws Exception {
        when(accountService.setupMainAccount(testSetupRequest.getUserId(), testSetupRequest.getAccountId())).thenReturn(0);

        mockMvc.perform(post("/api/v1/account/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSetupRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    void setupMainAccount_badRequest_blankUserId() throws Exception {
        SetupMainAccountRequest req = new SetupMainAccountRequest();
        req.setUserId(null);
        req.setAccountId(testAccountId);

        mockMvc.perform(post("/api/v1/account/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).setupMainAccount(any(), any());
    }

    @Test
    void setupMainAccount_badRequest_blankAccountId() throws Exception {
        SetupMainAccountRequest req = new SetupMainAccountRequest();
        req.setUserId(testUserId);
        req.setAccountId("   ");

        mockMvc.perform(post("/api/v1/account/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).setupMainAccount(any(), any());
    }

    @Test
    void setupMainAccount_serviceThrows() throws Exception {
        when(accountService.setupMainAccount(testSetupRequest.getUserId(), testSetupRequest.getAccountId())).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(post("/api/v1/account/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSetupRequest)))
                .andExpect(status().isInternalServerError());
    }

    // ------------------ updateAccountDetail ------------------
    @Test
    void updateAccountDetail_success() throws Exception {
        when(accountService.updateAccountDetail(testUpdateRequest.getAccountId(), testUpdateRequest.getNickname(), testUpdateRequest.getColor())).thenReturn(1);

        mockMvc.perform(post("/api/v1/account/name-color")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateAccountDetail_noContent() throws Exception {
        when(accountService.updateAccountDetail(testUpdateRequest.getAccountId(), testUpdateRequest.getNickname(), testUpdateRequest.getColor())).thenReturn(0);

        mockMvc.perform(post("/api/v1/account/name-color")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateAccountDetail_badRequest_blankAccountId() throws Exception {
        UpdateAccountDetailRequest req = new UpdateAccountDetailRequest();
        req.setAccountId("");

        mockMvc.perform(post("/api/v1/account/name-color")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).updateAccountDetail(any(), any(), any());
    }

    @Test
    void updateAccountDetail_serviceThrows() throws Exception {
        when(accountService.updateAccountDetail(testUpdateRequest.getAccountId(), testUpdateRequest.getNickname(), testUpdateRequest.getColor())).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(post("/api/v1/account/name-color")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isInternalServerError());
    }
}
