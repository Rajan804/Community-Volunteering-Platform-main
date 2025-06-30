package com.cvp.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cvp.exception.InvalidEntityException;
import com.cvp.model.Login;
import com.cvp.model.User;
import com.cvp.service.LoginService;


@RestController
@RequestMapping("/users")
public class UserLoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody Login loginRequest) throws InvalidEntityException {
        User authenticatedUser = loginService.login(loginRequest);
        return ResponseEntity.ok(authenticatedUser);
    }
}
