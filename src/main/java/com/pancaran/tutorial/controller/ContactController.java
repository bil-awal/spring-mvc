package com.pancaran.tutorial.controller;

import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.*;
import com.pancaran.tutorial.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse
                .<ContactResponse>builder()
                .message("Success")
                .data(contactResponse)
                .build();
    }

    @GetMapping(path = "/api/contacts/{contact_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> get(User user, @PathVariable("contact_id") String contact_id) {
        ContactResponse contactResponse = contactService.get(user, contact_id);
        return WebResponse
                .<ContactResponse>builder()
                .message("Success")
                .data(contactResponse)
                .build();
    }

    @PutMapping(
            path = "/api/contacts/{contact_id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update(
            User user,
            @PathVariable("contact_id") String contactID,
            @RequestBody UpdateContactRequest request
    ) {
        request.setId(contactID);

        ContactResponse contactResponse = contactService.update(user, request);
        return WebResponse
                .<ContactResponse>builder()
                .message("Success")
                .data(contactResponse)
                .build();
    }

    @DeleteMapping(path = "/api/contacts/{contactID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> delete(User user, @PathVariable("contactID") String contactID) {
        contactService.delete(user, contactID);

        return WebResponse
                .<String>builder()
                .message("Success Delete")
                .build();
    }

    @GetMapping(path = "/api/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<ContactResponse>> search(
            User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(
                    value = "page",
                    required = false,
                    defaultValue = "0"
            ) Integer page,
            @RequestParam(
                    value = "size",
                    required = true,
                    defaultValue = "10"
            ) Integer size
    ) {
        SearchContactRequest request = SearchContactRequest
                .builder()
                .page(page)
                .size(size)
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        Page<ContactResponse> contactResponses = contactService.search(user, request);
        return WebResponse
                .<List<ContactResponse>>builder()
                .data(contactResponses.getContent())
                .pagination(
                        PageResponse
                                .builder()
                                .currentPage(contactResponses.getNumber())
                                .totalPage(contactResponses.getTotalPages())
                                .size(contactResponses.getSize())
                                .build()
                )
                .build();
    }
}
