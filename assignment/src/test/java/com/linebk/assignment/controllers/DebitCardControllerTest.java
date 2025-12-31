package com.linebk.assignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linebk.assignment.models.dto.DebitCardDto;
import com.linebk.assignment.services.DebitCardService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DebitCardController.class)
class DebitCardControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        DebitCardService debitCardService() {
            return mock(DebitCardService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DebitCardService debitCardService;

    private DebitCardDto testCard;
    private DebitCardDto testCard2;
    private String testUserId;

    @BeforeEach
    void setUp() {
        reset(debitCardService);

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

    // ------------------ getDebitCardByUserId ------------------
    @Test
    void getDebitCardByUserId_success() throws Exception {
        when(debitCardService.getDebitCardByUserId(testUserId)).thenReturn(List.of(testCard));

        mockMvc.perform(get("/api/v1/debitcard").param("userid", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].userId", is(testUserId)));
    }

    @Test
    void getDebitCardByUserId_multipleCards() throws Exception {
        when(debitCardService.getDebitCardByUserId(testUserId)).thenReturn(List.of(testCard, testCard2));

        mockMvc.perform(get("/api/v1/debitcard").param("userid", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(debitCardService, times(1)).getDebitCardByUserId(testUserId);
    }

    @Test
    void getDebitCardByUserId_noContent() throws Exception {
        when(debitCardService.getDebitCardByUserId(testUserId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/debitcard").param("userid", testUserId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getDebitCardByUserId_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/debitcard").param("userid", " "))
                .andExpect(status().isBadRequest());

        verify(debitCardService, never()).getDebitCardByUserId(any());
    }

    @Test
    void getDebitCardByUserId_internalServerError() throws Exception {
        when(debitCardService.getDebitCardByUserId(testUserId)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/debitcard").param("userid", testUserId))
                .andExpect(status().isInternalServerError());
    }
}


