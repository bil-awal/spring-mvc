package com.pancaran.tutorial.controller;

import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.AddressResponse;
import com.pancaran.tutorial.model.CreateAddressRequest;
import com.pancaran.tutorial.model.UpdateAddressRequest;
import com.pancaran.tutorial.model.WebResponse;
import com.pancaran.tutorial.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{contact_id}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(
            User user,
            @RequestBody CreateAddressRequest request,
            @PathVariable("contact_id") String contactID
    ) {
        request.setContactId(contactID);
        AddressResponse addressResponse = addressService.create(user, request);
        return WebResponse
                .<AddressResponse>builder()
                .message("Create Success")
                .data(addressResponse)
                .build();
    }

    @GetMapping(
            path = "/api/contacts/{contact_id}/addresses/{address_id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(
            User user,
            @PathVariable("contact_id") String contactID,
            @PathVariable("address_id") String addressID
            ) {
        AddressResponse addressResponse = addressService.get(user, contactID, addressID);
        return WebResponse
                .<AddressResponse>builder()
                .message("Get Success")
                .data(addressResponse)
                .build();
    }

    @PutMapping(
            path = "/api/contacts/{contact_id}/addresses/{address_id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(
            User user,
            @RequestBody UpdateAddressRequest request,
            @PathVariable("contact_id") String contactID,
            @PathVariable("address_id") String addressID
    ) {
        request.setContactId(contactID);
        request.setAddressId(addressID);

        AddressResponse addressResponse = addressService.update(user, request);
        return WebResponse
                .<AddressResponse>builder()
                .message("Update Success")
                .data(addressResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/contacts/{contact_id}/addresses/{address_id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(
            User user,
            @PathVariable("contact_id") String contactID,
            @PathVariable("address_id") String addressID
    ) {
        addressService.delete(user, contactID, addressID);
        return WebResponse
                .<String>builder()
                .message("Delete Success")
                .build();
    }

    @GetMapping(
            path = "/api/contacts/{contact_id}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> list(
            User user,
            @PathVariable("contact_id") String contactID
    ) {
        List<AddressResponse> addressResponses = addressService.list(user, contactID);
        return WebResponse
                .<List<AddressResponse>>builder()
                .message("Get List Success")
                .data(addressResponses)
                .build();
    }
}
