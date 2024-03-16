package com.pancaran.tutorial.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.LoginUserRequest;
import com.pancaran.tutorial.model.TokenResponse;
import com.pancaran.tutorial.model.WebResponse;
import com.pancaran.tutorial.repository.UserRepository;
import com.pancaran.tutorial.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        objectMapper = new ObjectMapper();
    }

    @Test
    void failedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("test");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );

                    assertNotNull(response.getErrors());
                })
        ;
    }

    @Test
    void failedWrongPassword() throws Exception {
        User user = new User();
        user.setName("Test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("salah");

        mockMvc.perform(
                        post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );

                    assertNotNull(response.getErrors());
                })
        ;
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setUsername("john");
        user.setPassword(BCrypt.hashpw("secret123", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("john");
        request.setPassword("secret123");

        mockMvc.perform(
                        post("/api/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<TokenResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );

                    assertNull(response.getErrors());
                    assertNotNull(response.getData().getToken());
                    assertNotNull(response.getData().getExpiredAt());

                    User userDB = userRepository.findById("john").orElse(null);
                    assertNotNull(userDB);
                    assertEquals(userDB.getToken(), response.getData().getToken());
                    assertEquals(userDB.getToken_expire(), response.getData().getExpiredAt());
                })
        ;
    }

    @Test
    void logoutFailed() throws Exception {
        mockMvc.perform(
                        delete("/api/auth/logout")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setUsername("john");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setToken("token");
        user.setToken_expire(Instant.now().plusSeconds(7L * 24 * 60 * 60).toEpochMilli());
        userRepository.save(user);

        mockMvc.perform(
                        delete("/api/auth/logout")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertEquals("Success", response.getMessage());

                    User userDB = userRepository.findById("john").orElse(null);
                    assertNotNull(userDB);
                    assertNull(userDB.getToken());
                    assertNull(userDB.getToken_expire());

                });
    }
}