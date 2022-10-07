package org.polytech.covidapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.services.UserServices;

@RestController
public class AuthController {

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final UserRepository userRepository;

    @PostMapping(path = "/public/signin/{email}")
    public ResponseEntity<Void> login(@PathVariable("email") String email) {

        return ResponseEntity.ok().build();
    }

}
