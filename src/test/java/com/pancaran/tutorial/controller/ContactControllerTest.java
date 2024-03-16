package com.pancaran.tutorial.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancaran.tutorial.entity.Contact;
import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.ContactResponse;
import com.pancaran.tutorial.model.CreateContactRequest;
import com.pancaran.tutorial.model.WebResponse;
import com.pancaran.tutorial.repository.ContactRepository;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("John Doe");
        user.setUsername("john");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setToken("token");
        user.setToken_expire(Instant.now().plusSeconds((long) 24 * 60 * 60).toEpochMilli());
        userRepository.save(user);
    }

    @Test
    void createContactWrongEmail() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("wrong!mail.com");

        mockMvc.perform(
                post("/api/contacts")
                        .header("X-API-TOKEN", "token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                           result.getResponse().getContentAsString(),
                           new TypeReference<WebResponse<String>>() {});

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void createContactInvalidPhone() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@mail.com");
        request.setPhone("(+62)-851-5652-2750");

        mockMvc.perform(
                        post("/api/contacts")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<ContactResponse>>() {});

                    assertNotNull(response.getErrors());
                    assertEquals("phone: Invalid phone number", response.getErrors());
                });
    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@mail.com");
        request.setPhone("(62) 851-5652-2750");

        mockMvc.perform(
                        post("/api/contacts")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals("John", response.getData().getFirstName());
                    assertEquals("Doe", response.getData().getLastName());
                    assertEquals("john@mail.com", response.getData().getEmail());
                });
    }

    @Test
    void getContactNotFound() throws Exception {
        mockMvc.perform(
                        post("/api/contacts/123")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isNotFound())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {});

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepository.findById("john").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john@mail.com");
        contact.setPhone("(62) 851-5652-2750");
        contactRepository.save(contact);

        mockMvc.perform(
                        get("/api/contacts/" + contact.getId())
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals(contact.getId(), response.getData().getId());
                    assertEquals(contact.getFirstName(), response.getData().getFirstName());
                    assertEquals(contact.getLastName(), response.getData().getLastName());
                    assertEquals(contact.getEmail(), response.getData().getEmail());
                    assertEquals(contact.getPhone(), response.getData().getPhone());
                });
    }

    @Test
    void updateContactWrongEmail() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("wrong!mail.com");

        mockMvc.perform(
                        put("/api/contacts/123")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {});

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void updateContactSuccess() throws Exception {
        User user = userRepository.findById("john").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john@mail.com");
        contact.setPhone("(62) 851-5652-2750");
        contactRepository.save(contact);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Yana");
        request.setLastName("Yoga");
        request.setEmail("yoga@mail.com");
        request.setPhone("0851-5652-2750");

        mockMvc.perform(
                        put("/api/contacts/" + contact.getId())
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals(request.getFirstName(), response.getData().getFirstName());
                    assertEquals(request.getLastName(), response.getData().getLastName());
                    assertEquals(request.getEmail(), response.getData().getEmail());
                    assertEquals(request.getPhone(), response.getData().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void deleteContactNotFound() throws Exception {
        mockMvc.perform(
                        delete("/api/contacts/123")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isNotFound())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {});

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void deleteContactSuccess() throws Exception {
        User user = userRepository.findById("john").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john@mail.com");
        contact.setPhone("(62) 851-5652-2750");
        contactRepository.save(contact);

        mockMvc.perform(
                        delete("/api/contacts/" + contact.getId())
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals("Success Delete", response.getMessage());
                });
    }

    @Test
    void searchNotFound() throws Exception {
        mockMvc.perform(
                        get("/api/contacts")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(0, response.getPagination().getCurrentPage());
                    assertEquals(0, response.getPagination().getTotalPage());
                    assertEquals(10, response.getPagination().getSize());
                });
    }

    @Test
    void searchSuccess() throws Exception {
        User user = userRepository.findById("john").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());
            contact.setUser(user);
            contact.setFirstName("John " + i);
            contact.setLastName("Doe");
            contact.setEmail("john@mail.com");
            contact.setPhone("(62) 851-5652-2750");
            contactRepository.save(contact);
        }

        mockMvc.perform(
                        get("/api/contacts")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("name", "john 15")
                                .queryParam("email", "john@mail")
                                .queryParam("phone", "2750")
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals(0, response.getPagination().getCurrentPage());
                    assertEquals(1, response.getPagination().getTotalPage());
                    assertEquals(10, response.getPagination().getSize());
                });
    }
}
