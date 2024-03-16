package com.pancaran.tutorial.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancaran.tutorial.entity.Address;
import com.pancaran.tutorial.entity.Contact;
import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.AddressResponse;
import com.pancaran.tutorial.model.CreateAddressRequest;
import com.pancaran.tutorial.model.UpdateAddressRequest;
import com.pancaran.tutorial.model.WebResponse;
import com.pancaran.tutorial.repository.AddressRepository;
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
public class AddressControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("John Doe");
        user.setUsername("john");
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
        user.setToken("token");
        user.setToken_expire(Instant.now().plusSeconds((long) 24 * 60 * 60).toEpochMilli());
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("john");
        contact.setUser(user);
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john@mail.com");
        contact.setPhone("(62) 851-5652-2750");
        contactRepository.save(contact);
    }

    @Test
    void createAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");
        request.setZipCode(null);

        mockMvc.perform(
                post("/api/contacts/john/addresses")
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
    void createAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Jl. Pacing Raya");
        request.setCity("Bekasi");
        request.setProvince("Jawa Barat");
        request.setCountry("Indonesia");
        request.setZipCode(17540);

        mockMvc.perform(
                        post("/api/contacts/john/addresses")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<AddressResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<AddressResponse>>() {});

                    assertNull(response.getErrors());
                    assertEquals(request.getStreet(), response.getData().getStreet());
                    assertEquals(request.getCity(), response.getData().getCity());
                    assertEquals(request.getProvince(), response.getData().getProvince());
                    assertEquals(request.getCountry(), response.getData().getCountry());
                    assertEquals(request.getZipCode(), response.getData().getZipCode());
                });
    }

    @Test
    void getAddressNotFound() throws Exception {
        mockMvc.perform(
                        get("/api/contacts/john/addresses/123")
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
    void getAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("john").orElseThrow();

        Address address = new Address();
        address.setId("address-john");
        address.setContact(contact);
        address.setStreet("Jl. Pacing Raya");
        address.setCity("Bekasi");
        address.setProvince("Jawa Barat");
        address.setCountry("Indonesia");
        address.setZipCode(17540);
        addressRepository.save(address);

        mockMvc.perform(
                        get("/api/contacts/john/addresses/address-john")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<AddressResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals(address.getId(), response.getData().getId());
                    assertEquals(address.getStreet(), response.getData().getStreet());
                    assertEquals(address.getCity(), response.getData().getCity());
                    assertEquals(address.getProvince(), response.getData().getProvince());
                    assertEquals(address.getCountry(), response.getData().getCountry());
                    assertEquals(address.getZipCode(), response.getData().getZipCode());
                });
    }

    @Test
    void updateAddressBadRequest() throws Exception {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");
        request.setZipCode(null);

        mockMvc.perform(
                        put("/api/contacts/john/addresses/123")
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
    void updateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("john").orElseThrow();

        Address address = new Address();
        address.setId("address-john");
        address.setContact(contact);
        address.setStreet("Jl. Pacing Raya");
        address.setCity("Bekasi");
        address.setProvince("Jawa Barat");
        address.setCountry("Indonesia");
        address.setZipCode(17540);
        addressRepository.save(address);

        UpdateAddressRequest update = new UpdateAddressRequest();
        update.setStreet("Jl. Rengas Bandung");
        update.setCity("Cikarang");
        update.setProvince("Jawa Tengah");
        update.setCountry("Malaysia");
        update.setZipCode(12300);

        mockMvc.perform(
                        put("/api/contacts/john/addresses/address-john")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(update))
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<AddressResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals(update.getStreet(), response.getData().getStreet());
                    assertEquals(update.getCity(), response.getData().getCity());
                    assertEquals(update.getProvince(), response.getData().getProvince());
                    assertEquals(update.getCountry(), response.getData().getCountry());
                    assertEquals(update.getZipCode(), response.getData().getZipCode());
                });
    }

    @Test
    void deleteAddressNotFound() throws Exception {
        mockMvc.perform(
                        delete("/api/contacts/john/addresses/123")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isNotFound())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void deleteAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("john").orElseThrow();

        Address address = new Address();
        address.setId("address-john");
        address.setContact(contact);
        address.setStreet("Jl. Pacing Raya");
        address.setCity("Bekasi");
        address.setProvince("Jawa Barat");
        address.setCountry("Indonesia");
        address.setZipCode(17540);
        addressRepository.save(address);

        mockMvc.perform(
                        delete("/api/contacts/john/addresses/address-john")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<AddressResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                });
    }

    @Test
    void listAddressNotFound() throws Exception {
        mockMvc.perform(
                        get("/api/contacts/123/addresses")
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
    void listAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("john").orElseThrow();

        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setId("address-" + i);
            address.setContact(contact);
            address.setStreet("Jl. Pacing Raya");
            address.setCity("Bekasi");
            address.setProvince("Jawa Barat");
            address.setCountry("Indonesia");
            address.setZipCode(17540);
            addressRepository.save(address);
        }

        mockMvc.perform(
                        get("/api/contacts/john/addresses")
                                .header("X-API-TOKEN", "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());

                    WebResponse<List<AddressResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {});

                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                });
    }
}
