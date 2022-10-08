package org.polytech.covidapi.controllers;

import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/public/signin/{email}")
    public ResponseEntity<Void> login(@PathVariable("email") String email) {

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/public/signup")
    public ResponseEntity<Void> signup(@Param("first_name") String first_name, @Param("last_name") String last_name,
            @Param("email") String email, @Param("password") String password) {
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().build(); // TODO: return user
    }

}
