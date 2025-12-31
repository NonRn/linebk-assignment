package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.DebitCardDto;
import com.linebk.assignment.repositories.DebitCardRepository;
import com.linebk.assignment.services.impl.DebitCardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebitCardServiceTest {

    @Mock
    private DebitCardRepository debitCardRepository;

    @InjectMocks
    private DebitCardServiceImpl debitCardService;

    private DebitCardDto testCard;
    private DebitCardDto testCard2;
    private String testUserId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testUserId = "000018b0e1a211ef95a30242ac180002";

        testCard = new DebitCardDto();
        testCard.setCardId("ffdda5dae1a111ef95a30242ac180002");
        testCard.setUserId(testUserId);
        testCard.setName("My Debit Card");
        testCard.setIssuer("TestLab");
        testCard.setNumber("6821 5668 7876 2379");
        testCard.setStatus("Active");
        testCard.setColor("#00a1e2");
        testCard.setBorderColor("#ffffff");

        testCard2 = new DebitCardDto();
        testCard2.setCardId("ffddb7afe1a111ef95a30242ac180002");
        testCard2.setUserId(testUserId);
        testCard2.setName("2nd Debit Card");
        testCard2.setIssuer("TestLab");
        testCard2.setNumber("1895 6835 8492 1957");
        testCard2.setStatus("Active");
        testCard2.setColor("#24c875");
        testCard2.setBorderColor("#ffffff");
    }

    /* ==================== Test GetDebitCardByUserId ==================== */
    @Test
    void testGetDebitCardByUserId_Success() {
        // Arrange
        List<DebitCardDto> cardList = new ArrayList<>();
        cardList.add(testCard);

        when(debitCardRepository.getDebitCardByUserId(testUserId)).thenReturn(cardList);

        // Act
        List<DebitCardDto> result = debitCardService.getDebitCardByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(cardList.size(), result.size());
    }

    @Test
    void testGetDebitCardByUserId_EmptyResult() {
        // Arrange
        when(debitCardRepository.getDebitCardByUserId(testUserId)).thenReturn(new ArrayList<>());

        // Act
        List<DebitCardDto> result = debitCardService.getDebitCardByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDebitCardByUserId_MultipleCards() {
        // Arrange
        List<DebitCardDto> cardList = new ArrayList<>();
        cardList.add(testCard);
        cardList.add(testCard2);

        when(debitCardRepository.getDebitCardByUserId(testUserId)).thenReturn(cardList);

        // Act
        List<DebitCardDto> result = debitCardService.getDebitCardByUserId(testUserId);

        // Assert
        assertEquals(cardList.size(), result.size());
    }

    @Test
    void testGetDebitCardByUserId_VerifyRepositoryCalled() {
        // Arrange
        when(debitCardRepository.getDebitCardByUserId(anyString())).thenReturn(new ArrayList<>());

        // Act
        debitCardService.getDebitCardByUserId(testUserId);

        // Assert
        verify(debitCardRepository, times(1)).getDebitCardByUserId(testUserId);
    }
}

