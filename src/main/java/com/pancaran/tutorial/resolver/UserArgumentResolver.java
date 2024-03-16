package com.pancaran.tutorial.resolver;

import com.pancaran.tutorial.entity.User;
import com.pancaran.tutorial.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Component
@Slf4j
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Autowired
    public UserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = servletRequest.getHeader("X-API-TOKEN");

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is Required");
        }

        User user = userRepository
                .findFirstByToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unauthorized"
                ));

        Instant tokenExpiration = Instant.ofEpochMilli(user.getToken_expire());
        Instant currentInstant = Instant.now();

        if (tokenExpiration.isBefore(currentInstant)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Expired");
        }

        return user;
    }
}
