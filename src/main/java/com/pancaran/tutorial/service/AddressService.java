package com.pancaran.tutorial.service;

import com.pancaran.tutorial.entity.Address;
import com.pancaran.tutorial.entity.Contact;
import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.AddressResponse;
import com.pancaran.tutorial.model.CreateAddressRequest;
import com.pancaran.tutorial.model.UpdateAddressRequest;
import com.pancaran.tutorial.repository.AddressRepository;
import com.pancaran.tutorial.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse
                .builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .province(address.getProvince())
                .country(address.getCountry())
                .zipCode(address.getZipCode())
                .build();
    }

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository
                .findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Contact is Not Found"
                        )
                );

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());
        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String contactID, String addressID) {
        Contact contact = contactRepository
                .findFirstByUserAndId(user, contactID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contact is Not Found")
                );

        Address address = addressRepository
                .findFirstByContactAndId(contact, addressID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Address is Not Found")
                );

        return toAddressResponse(address);
    }

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository
                .findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contact is Not Found")
                );

        Address address = addressRepository
                .findFirstByContactAndId(contact, request.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Address is Not Found")
                );

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());
        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional
    public void delete(User user, String contactID, String addressID) {
        Contact contact = contactRepository
                .findFirstByUserAndId(user, contactID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contact is Not Found")
                );

        Address address = addressRepository
                .findFirstByContactAndId(contact, addressID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Address is Not Found")
                );

        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String contactID) {
        Contact contact = contactRepository
                .findFirstByUserAndId(user, contactID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contact is Not Found")
                );

        List<Address> addresses = addressRepository.findAllByContact(contact);
        return addresses.stream().map(this::toAddressResponse).toList();
    }
}
