package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.AccountDto;
import com.linebk.assignment.models.dto.AccountFlagDto;
import com.linebk.assignment.repositories.AccountRepository;
import com.linebk.assignment.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDto testAccount;
    private AccountDto testAccount2;
    private AccountFlagDto testFlag;
    private AccountFlagDto testFlag2;
    private String testUserId;
    private String testAccountId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testUserId = "000018b0e1a211ef95a30242ac180002";
        testAccountId = "000028aae1a211ef95a30242ac180002";
        String testAccountId2 = "000018b0e1a211ef95a30242ac180002";

        testAccount = new AccountDto();
        testAccount.setAccountId(testAccountId);
        testAccount.setUserId(testUserId);
        testAccount.setType("saving-account");
        testAccount.setCurrency("THB");
        testAccount.setAccountNumber("568-2-45295");
        testAccount.setIssuer("TestLab");

        testAccount = new AccountDto();
        testAccount.setAccountId(testAccountId);
        testAccount.setUserId(testUserId);
        testAccount.setType("saving-account");
        testAccount.setCurrency("THB");
        testAccount.setAccountNumber("568-2-45295");
        testAccount.setIssuer("TestLab");

        testAccount2 = new AccountDto();
        testAccount2.setAccountId(testAccountId2);
        testAccount2.setUserId(testUserId);
        testAccount2.setType("saving-account");
        testAccount2.setCurrency("THB");
        testAccount2.setAccountNumber("568-2-94760");
        testAccount2.setIssuer("TestLab");

        testFlag = new AccountFlagDto();
        testFlag.setAccountId(testAccountId);
        testFlag.setFlagType("system");
        testFlag.setFlagValue("Flag5");

        testFlag2 = new AccountFlagDto();
        testFlag2.setAccountId(testAccountId2);
        testFlag2.setFlagType("system");
        testFlag2.setFlagValue("Overdue");
    }

    /* ==================== Test GetAccountByUserId ==================== */
    @Test
    void testGetAccountByUserId_Success() {
        // Arrange
        List<AccountDto> accountList = new ArrayList<>();
        accountList.add(testAccount);
        List<AccountFlagDto> flagList = new ArrayList<>();
        flagList.add(testFlag);

        when(accountRepository.getAccountByUserId(testUserId)).thenReturn(accountList);
        when(accountRepository.getAccountFlagByAccountIdList(anyList())).thenReturn(flagList);

        // Act
        List<AccountDto> result = accountService.getAccountByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(accountList.size(), result.size());
        assertEquals(testAccountId, result.getFirst().getAccountId());
        assertEquals(1, result.getFirst().getFlags().size());
    }

    @Test
    void testGetAccountByUserId_EmptyResult() {
        // Arrange
        when(accountRepository.getAccountByUserId(testUserId)).thenReturn(new ArrayList<>());

        // Act
        List<AccountDto> result = accountService.getAccountByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(accountRepository, never()).getAccountFlagByAccountIdList(anyList());
    }

    @Test
    void testGetAccountByUserId_MultipleAccountsWithFlags() {
        // Arrange
        List<AccountDto> accountList = new ArrayList<>();
        accountList.add(testAccount);
        accountList.add(testAccount2);

        List<AccountFlagDto> flagList = new ArrayList<>();
        flagList.add(testFlag);
        flagList.add(testFlag2);

        when(accountRepository.getAccountByUserId(testUserId)).thenReturn(accountList);
        when(accountRepository.getAccountFlagByAccountIdList(anyList())).thenReturn(flagList);

        // Act
        List<AccountDto> result = accountService.getAccountByUserId(testUserId);

        // Assert
        assertEquals(accountList.size(), result.size());
        assertEquals(1, result.get(0).getFlags().size());
        assertEquals("Flag5", result.get(0).getFlags().getFirst().getFlagValue());
        assertEquals(1, result.get(1).getFlags().size());
        assertEquals("Overdue", result.get(1).getFlags().getFirst().getFlagValue());
    }

    /* ==================== Test withdrawAmount ==================== */
    @Test
    void testWithdrawAmount_Success() {
        // Arrange
        BigDecimal currentBalance = new BigDecimal("1000.00");
        BigDecimal withdrawAmount = new BigDecimal("200.00");
        BigDecimal expectedNewBalance = new BigDecimal("800.00");

        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.getAccountBalance(testAccountId)).thenReturn(currentBalance);
        when(accountRepository.updateAccountBalance(testAccountId, expectedNewBalance)).thenReturn(1);

        // Act
        int result = accountService.withdrawAmount(testAccountId, withdrawAmount);

        // Assert
        assertEquals(1, result);
        verify(accountRepository, times(1)).isAccountExists(testAccountId);
        verify(accountRepository, times(1)).getAccountBalance(testAccountId);
        verify(accountRepository, times(1)).updateAccountBalance(testAccountId, expectedNewBalance);
    }

    @Test
    void testWithdrawAmount_AccountNotFound() {
        // Arrange
        BigDecimal withdrawAmount = new BigDecimal("200.00");
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(false);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> accountService.withdrawAmount(testAccountId, withdrawAmount)
        );
        verify(accountRepository, never()).updateAccountBalance(anyString(), any());
    }

    @Test
    void testWithdrawAmount_FailedToRetrieveBalance() {
        // Arrange
        BigDecimal withdrawAmount = new BigDecimal("200.00");
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.getAccountBalance(testAccountId)).thenReturn(null);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> accountService.withdrawAmount(testAccountId, withdrawAmount)
        );
        verify(accountRepository, never()).updateAccountBalance(anyString(), any());
    }

    @Test
    void testWithdrawAmount_InsufficientBalance() {
        // Arrange
        BigDecimal currentBalance = new BigDecimal("100.00");
        BigDecimal withdrawAmount = new BigDecimal("200.00");

        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.getAccountBalance(testAccountId)).thenReturn(currentBalance);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> accountService.withdrawAmount(testAccountId, withdrawAmount)
        );
        verify(accountRepository, never()).updateAccountBalance(anyString(), any());
    }

    // ==================== Test setupMainAccount ====================
    @Test
    void testSetupMainAccount_Success() {
        // Arrange
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.setupMainAccount(testUserId, testAccountId)).thenReturn(1);

        // Act
        int result = accountService.setupMainAccount(testUserId, testAccountId);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testSetupMainAccount_AccountNotFound() {
        // Arrange
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(false);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> accountService.setupMainAccount(testUserId, testAccountId)
        );
        verify(accountRepository, never()).setupMainAccount(anyString(), anyString());
    }

    @Test
    void testSetupMainAccount_Failure() {
        // Arrange
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.setupMainAccount(testUserId, testAccountId)).thenReturn(0);

        // Act
        int result = accountService.setupMainAccount(testUserId, testAccountId);

        // Assert
        assertEquals(0, result);
    }

    // ==================== Test updateAccountDetail ====================
    @Test
    void testUpdateAccountDetail_Success() {
        // Arrange
        String nickname = "Travel";
        String color = "#24c875";

        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.updateAccountDetail(testAccountId, nickname, color)).thenReturn(1);

        // Act
        int result = accountService.updateAccountDetail(testAccountId, nickname, color);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testUpdateAccountDetail_AccountNotFound() {
        // Arrange
        String nickname = "Travel";
        String color = "#24c875";
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(false);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> accountService.updateAccountDetail(testAccountId, nickname, color)
        );
        verify(accountRepository, never()).updateAccountDetail(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateAccountDetail_NullValue() {
        // Arrange
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.updateAccountDetail(testAccountId, null, null)).thenReturn(1);

        // Act
        int result = accountService.updateAccountDetail(testAccountId, null, null);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testUpdateAccountDetail_Failure() {
        // Arrange
        when(accountRepository.isAccountExists(testAccountId)).thenReturn(true);
        when(accountRepository.updateAccountDetail(testAccountId, "Nickname", "#FF5733")).thenReturn(0);

        // Act
        int result = accountService.updateAccountDetail(testAccountId, "Nickname", "#FF5733");

        // Assert
        assertEquals(0, result);
        verify(accountRepository, times(1)).updateAccountDetail(testAccountId, "Nickname", "#FF5733");
    }
}

