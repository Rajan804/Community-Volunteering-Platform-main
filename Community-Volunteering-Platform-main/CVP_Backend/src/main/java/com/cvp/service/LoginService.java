package com.cvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cvp.exception.InvalidEntityException;
import com.cvp.model.Login;
import com.cvp.model.User;
import com.cvp.repository.UserRepository;


@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user using email and password.
     *
     * @param loginRequest the login credentials provided by the user
     * @return the authenticated User if credentials are valid
     * @throws InvalidEntityException if authentication fails
     */
    public User login(Login loginRequest) throws InvalidEntityException {
        // Retrieve user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidEntityException("Invalid email or password"));

        // Validate the provided password against the stored encoded password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidEntityException("Invalid email or password");
        }

        // If authentication is successful, return the user
        return user;
    }
}

