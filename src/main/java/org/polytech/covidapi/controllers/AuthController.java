package org.polytech.covidapi.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserServices userService;

    AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/private/me")
    public UserDetails getUserDetails(@PathVariable("email") String email) {
        UserDetails details = userService.loadUserByUsername(email);
        return details;
    }

    @PostMapping(path = "/public/signin")
    public ResponseEntity<Void> login(@RequestParam("email") String email, @RequestParam("password") String password) {

        String encodePassword = passwordEncoder.encode(password);
        User user = userRepository.findFirstByEmailAndPassword(email, encodePassword);
        if (user != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/public/signup")
    public ResponseEntity<Void> signup(@Param("first_name") String first_name, @Param("last_name") String last_name,
            @Param("email") String email, @Param("password") String password) {
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(email);
        List<String> roles = Arrays.asList("USER");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().build(); // TODO: return user
    }

    @PostMapping(path = "/private/superAdmin/signup/admin")
    public ResponseEntity<Void> signupAdmin(@Param("first_name") String first_name,
            @Param("last_name") String last_name,
            @Param("email") String email, @Param("password") String password) {
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(email);
        List<String> roles = Arrays.asList("ADMIN");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/private/admin/signup/doctors")
    public ResponseEntity<Void> signupDoctors(@Param("first_name") String first_name,
            @Param("last_name") String last_name,
            @Param("email") String email, @Param("password") String password) {
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(email);
        List<String> roles = Arrays.asList("DOCTORS");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

}
