package com.linebk.assignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linebk.assignment.models.dto.AuthRequest;
import com.linebk.assignment.models.dto.UserDto;
import com.linebk.assignment.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService() {
            return mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private UserDto testUser;
    private AuthRequest testAuthRequest;
    private String testUserId;

    @BeforeEach
    void setUp() {
        reset(userService);

        testUserId = "000018b0e1a211ef95a30242ac180002";

        testUser = UserDto.builder()
                .userId(testUserId)
                .name("User_000043b3e1a211ef95a30242ac180002")
                .greetingText("Hello User_000043b3e1a211ef95a30242ac180002")
                .build();

        testAuthRequest = new AuthRequest();
        testAuthRequest.setUserid(testUserId);
        testAuthRequest.setPasscode("123456");
    }

    // ------------------ getUserById ------------------

    @Test
    void getUserById_success() throws Exception {
        when(userService.getUserById(testUserId)).thenReturn(testUser);

        mockMvc.perform(get("/api/v1/user").param("userid", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(testUserId)));
    }

    @Test
    void getUserById_noContent() throws Exception {
        when(userService.getUserById(testUserId)).thenReturn(null);

        mockMvc.perform(get("/api/v1/user").param("userid", testUserId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserById_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/user").param("userid", ""))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void getUserById_serviceThrows() throws Exception {
        when(userService.getUserById(testUserId)).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(get("/api/v1/user").param("userid", testUserId))
                .andExpect(status().isInternalServerError());

        verify(userService, times(1)).getUserById(testUserId);
    }

    // ------------------ authenticatePasscodeUser ------------------
    @Test
    void authenticatePasscodeUser_success() throws Exception {
        when(userService.getUserById(testUserId)).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/user/auth/passcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticatePasscodeUser_userNotFound() throws Exception {
        when(userService.getUserById(testUserId)).thenReturn(null);

        mockMvc.perform(post("/api/v1/user/auth/passcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatePasscodeUser_badRequest() throws Exception {
        mockMvc.perform(post("/api/v1/user/auth/passcode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void authenticatePasscodeUser_badRequest_missingUserId() throws Exception {
        String jsonWithPasscodeOnly = "{\"passcode\":\"123456\"}";

        mockMvc.perform(post("/api/v1/user/auth/passcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPasscodeOnly))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void authenticatePasscodeUser_badRequest_invalidPasscode() throws Exception {
        String jsonWithInvalidPasscode = "{\"userid\":\"" + testUserId + "\", \"passcode\":\"1234\"}";

        mockMvc.perform(post("/api/v1/user/auth/passcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithInvalidPasscode))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void authenticatePasscodeUser_badRequest_missingPasscode() throws Exception {
        String jsonWithUserIdOnly = "{\"userid\":\"" + testUserId + "\"}";

        mockMvc.perform(post("/api/v1/user/auth/passcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithUserIdOnly))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void authenticatePasscodeUser_serviceThrows() throws Exception {
        when(userService.getUserById(testUserId)).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(post("/api/v1/user/auth/passcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthRequest)))
                .andExpect(status().isInternalServerError());
    }

    // ------------------ authenticateLoginUser ------------------
    @Test
    void authenticateLoginUser_success() throws Exception {
        when(userService.getUserById(testUserId)).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateLoginUser_userNotFound() throws Exception {
        when(userService.getUserById(testUserId)).thenReturn(null);

        mockMvc.perform(post("/api/v1/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticateLoginUser_badRequest_noParameter() throws Exception {
        mockMvc.perform(post("/api/v1/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void authenticateLoginUser_missingUserIdInBody() throws Exception {
        String jsonWithPasscodeOnly = "{\"passcode\":\"123456\"}";

        mockMvc.perform(post("/api/v1/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPasscodeOnly))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void authenticateLoginUser_missingPasscodeInBody() throws Exception {
        String jsonWithUserIdOnly = "{\"userid\":\"" + testUserId + "\"}";

        mockMvc.perform(post("/api/v1/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithUserIdOnly))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void authenticateLoginUser_serviceThrows() throws Exception {
        when(userService.getUserById(testUserId)).thenThrow(new RuntimeException("db error"));

        mockMvc.perform(post("/api/v1/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthRequest)))
                .andExpect(status().isInternalServerError());
    }
}
