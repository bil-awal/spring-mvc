package com.pancaran.tutorial.service;

import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.model.LoginUserRequest;
import com.pancaran.tutorial.model.TokenResponse;
import com.pancaran.tutorial.repository.UserRepository;
import com.pancaran.tutorial.security.BCrypt;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository
                .findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Username / Password wrong!"
                ));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setToken_expire(System.currentTimeMillis());
            userRepository.save(user);

            return TokenResponse
                    .builder()
                    .token(user.getToken())
                    .expiredAt(user.getToken_expire())
                    .build();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Username / Password wrong!"
            );
        }
    } private static Instant nextWeek() {
        return Instant.now().plusSeconds(7L * 24 * 60 * 60);
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setToken_expire(null);

        userRepository.save(user);
    }
}
