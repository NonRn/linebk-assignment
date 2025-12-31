package com.linebk.assignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linebk.assignment.models.dto.BannerDto;
import com.linebk.assignment.services.BannerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BannerController.class)
class BannerControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        BannerService bannerService() {
            return mock(BannerService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BannerService bannerService;

    private BannerDto testBanner;
    private BannerDto testBanner2;
    private String testUserId;

    @BeforeEach
    void setUp() {
        reset(bannerService);

        testUserId = "000018b0e1a211ef95a30242ac180002";

        testBanner = new BannerDto();
        testBanner.setUserId(testUserId);
        testBanner.setTitle("Want some money?");
        testBanner.setDescription("You can start applying");
        testBanner.setImage("https://dummyimage.com/54x54/999/fff");

        testBanner2 = new BannerDto();
        testBanner2.setUserId(testUserId);
        testBanner2.setTitle("Save More, Get More");
        testBanner2.setDescription("Special offer for savings account");
        testBanner2.setImage("https://dummyimage.com/54x54/999/fff");
    }

    // ------------------ getBannerByUserId ------------------
    @Test
    void getBannerByUserId_success() throws Exception {
        when(bannerService.getBannerByUserId(testUserId)).thenReturn(List.of(testBanner));

        mockMvc.perform(get("/api/v1/banner").param("userid", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].userId", is(testUserId)));
    }

    @Test
    void getBannerByUserId_multipleBanners() throws Exception {
        when(bannerService.getBannerByUserId(testUserId)).thenReturn(List.of(testBanner, testBanner2));

        mockMvc.perform(get("/api/v1/banner").param("userid", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(bannerService, times(1)).getBannerByUserId(testUserId);
    }

    @Test
    void getBannerByUserId_noContent() throws Exception {
        when(bannerService.getBannerByUserId(testUserId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/banner").param("userid", testUserId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBannerByUserId_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/banner").param("userid", ""))
                .andExpect(status().isBadRequest());

        verify(bannerService, never()).getBannerByUserId(any());
    }

    @Test
    void getBannerByUserId_internalServerError() throws Exception {
        when(bannerService.getBannerByUserId(testUserId)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/banner").param("userid", testUserId))
                .andExpect(status().isInternalServerError());
    }
}


