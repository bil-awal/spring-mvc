package com.pancaran.tutorial.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
                post("/api/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<WebResponse<String>>() {
                    });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void registerDuplicate() throws Exception {
        User user = new User();
        user.setUsername("billy");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setName("Bil Awal");
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("billy");
        request.setPassword("secret");
        request.setName("Bil Awal");

        mockMvc.perform(
                post("/api/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void registerSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("billy");
        request.setPassword("secret");
        request.setName("Bil Awal");

        mockMvc.perform(
                post("/api/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<WebResponse<String>>() {
                    });

            assertEquals("OK", response.getData());
        });
    }

    @Test
    void tokenUnauthorized() throws Exception {
        mockMvc.perform(
                    get("/api/user/info")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "not_found"))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void tokenNotSend() throws Exception {
        mockMvc
                .perform(
                        get("/api/user/info")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void tokenExpired() throws Exception {
        User user = new User();
        user.setName("John");
        user.setUsername("john");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setToken("token");
        user.setToken_expire(Instant.now().minusSeconds(7L * 24 * 60 * 60).toEpochMilli());
        userRepository.save(user);

        mockMvc.perform(
                        get("/api/user/info")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "token"))
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {});

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void tokenSuccess() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setUsername("john");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setToken("token");
        user.setToken_expire(Instant.now().plusSeconds(7L * 24 * 60 * 60).toEpochMilli());
        userRepository.save(user);

        mockMvc.perform(
                        get("/api/user/info")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-API-TOKEN", "token"))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    WebResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<UserResponse>>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals("John", response.getData().getName());
                    assertEquals("john", response.getData().getUsername());
                });
    }

    @Test
    void updateUnauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();

        mockMvc.perform(
                        patch("/api/user/update")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(status().isUnauthorized())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void updateSuccess() throws Exception {
        User user = new User();
        user.setName("John");
        user.setUsername("john");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setToken("token");
        user.setToken_expire(Instant.now().plusSeconds(7L * 24 * 60 * 60).toEpochMilli());
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Yona");
        request.setPassword("kurang_secret");

        mockMvc.perform(
                        patch("/api/user/update")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<UserResponse>>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals("Yona", response.getData().getName());
                    assertEquals("john", response.getData().getUsername());

                    User userDB = userRepository.findById("john").orElse(null);
                    assertNotNull(userDB);
                    assertTrue(BCrypt.checkpw("kurang_secret", userDB.getPassword()));

                });
    }

}
