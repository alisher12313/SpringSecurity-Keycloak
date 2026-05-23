package com.pm.ex2.service;

import com.pm.ex2.dto.CreateUserRequest;
import com.pm.ex2.entity.User;
import com.pm.ex2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(CreateUserRequest createUserRequest) {
        User user = new User();

        user.setUsername(createUserRequest.getUsername());
        user.setPasswordHash(createUserRequest.getPassword());

        return userRepository.save(user);
    }
}
