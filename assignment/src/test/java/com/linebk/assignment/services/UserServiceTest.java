package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.UserDto;
import com.linebk.assignment.models.entities.User;
import com.linebk.assignment.models.entities.UserGreeting;
import com.linebk.assignment.repositories.UserGreetingJpaRepository;
import com.linebk.assignment.repositories.UserJpaRepository;
import com.linebk.assignment.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private UserGreetingJpaRepository userGreetingJpaRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private User testUser2;
    private UserGreeting testGreeting;
    private String testUserId;
    private String testUserId2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testUserId = "000018b0e1a211ef95a30242ac180002";
        testUserId2 = "00005323e1a211ef95a30242ac180002";

        testUser = new User();
        testUser.setUserId(testUserId);
        testUser.setName("User_000043b3e1a211ef95a30242ac180002");

        testUser2 = new User();
        testUser2.setUserId(testUserId2);
        testUser2.setName("User_00005323e1a211ef95a30242ac180002");

        testGreeting = new UserGreeting();
        testGreeting.setUserId(testUserId);
        testGreeting.setGreeting("Hello User_000043b3e1a211ef95a30242ac180002");
    }

    /* ==================== Test GetUserById ==================== */
    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userJpaRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userGreetingJpaRepository.findById(testUserId)).thenReturn(Optional.of(testGreeting));

        // Act
        UserDto result = userService.getUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result.getUserId());
        verify(userGreetingJpaRepository, times(1)).findById(testUserId);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        when(userJpaRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        UserDto result = userService.getUserById(testUserId);

        // Assert
        assertNull(result);
        verify(userGreetingJpaRepository, never()).findById(anyString());
    }

    @Test
    void testGetUserById_WithoutGreeting() {
        // Arrange
        when(userJpaRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userGreetingJpaRepository.findById(testUserId)).thenReturn(Optional.empty());

        // Act
        UserDto result = userService.getUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result.getUserId());
        assertNull(result.getGreetingText());
    }

    @Test
    void testGetUserById_WithNullUserId() {
        // Act
        UserDto result = userService.getUserById(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetUserById_WithBlankUserId() {
        // Act
        UserDto result = userService.getUserById("   ");

        // Assert
        assertNull(result);
    }

    /* ==================== Test getAllUserIds ==================== */
    @Test
    void testGetAllUserIds_Success() {
        // Arrange
        when(userJpaRepository.getAllUserIdsLimitNative()).thenReturn(
                java.util.Arrays.asList(testUserId, testUserId2)
        );

        // Act
        java.util.List<String> result = userService.getAllUserIds();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

}

