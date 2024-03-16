package com.pancaran.tutorial.service;

import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.RegisterUserRequest;
import com.pancaran.tutorial.model.UpdateUserRequest;
import com.pancaran.tutorial.model.UserResponse;
import com.pancaran.tutorial.repository.UserRepository;
import com.pancaran.tutorial.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        log.info("REQUEST: {}", request);
        validationService.validate(request);
        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        return UserResponse
                .builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();

    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        log.info("REQUEST: {}", request);
        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }


        userRepository.save(user);

        return UserResponse
                .builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }
}
