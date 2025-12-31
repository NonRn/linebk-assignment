package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.BannerDto;
import com.linebk.assignment.repositories.BannerRepository;
import com.linebk.assignment.services.impl.BannerServiceImpl;
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
class BannerServiceTest {

    @Mock
    private BannerRepository bannerRepository;

    @InjectMocks
    private BannerServiceImpl bannerService;

    private BannerDto testBanner;
    private BannerDto testBanner2;
    private String testUserId;

    @BeforeEach
    void setUp() {
        // Initialize test data
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

    /* ==================== Test GetBannerByUserId ==================== */
    @Test
    void testGetBannerByUserId_Success() {
        // Arrange
        List<BannerDto> bannerList = new ArrayList<>();
        bannerList.add(testBanner);

        when(bannerRepository.getBannerByUserId(testUserId)).thenReturn(bannerList);

        // Act
        List<BannerDto> result = bannerService.getBannerByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(bannerList.size(), result.size());
    }

    @Test
    void testGetBannerByUserId_EmptyResult() {
        // Arrange
        when(bannerRepository.getBannerByUserId(testUserId)).thenReturn(new ArrayList<>());

        // Act
        List<BannerDto> result = bannerService.getBannerByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetBannerByUserId_MultipleBanners() {
        // Arrange
        List<BannerDto> bannerList = new ArrayList<>();
        bannerList.add(testBanner);
        bannerList.add(testBanner2);

        when(bannerRepository.getBannerByUserId(testUserId)).thenReturn(bannerList);

        // Act
        List<BannerDto> result = bannerService.getBannerByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(bannerList.size(), result.size());
    }

    @Test
    void testGetBannerByUserId_VerifyRepositoryCalled() {
        // Arrange
        when(bannerRepository.getBannerByUserId(anyString())).thenReturn(new ArrayList<>());

        // Act
        bannerService.getBannerByUserId(testUserId);

        // Assert
        verify(bannerRepository, times(1)).getBannerByUserId(testUserId);
    }
}

