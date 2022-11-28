package org.polytech.covidapi.controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.dto.ProfileView;
import org.polytech.covidapi.dto.SignupUserView;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/private/me")
    public ResponseEntity<Object> getUserDetails() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = details.getUsername();
        Optional<User> user = userRepository.findFirstByEmail(userEmail);
        if (user.isPresent()) {
            return ResponseHandler.generateResponse("User successfully retrieved", HttpStatus.OK,
                    new ProfileView(user.get()));
        }
        return ResponseHandler.generateResponse("User not found", HttpStatus.NOT_FOUND, null);
    }

    @PostMapping(path = "/public/signin")
    public ResponseEntity<Object> login(@RequestParam("email") String email,
            @RequestParam("password") String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Invalid credentials", HttpStatus.BAD_REQUEST, null);
        }

        User user = userRepository.findFirstByEmail(email).get();
        if (user.getDisabled()) {
            return ResponseHandler.generateResponse("User is disabled", HttpStatus.BAD_REQUEST, null);
        }

        return ResponseHandler.generateResponse("User successfully logged in", HttpStatus.OK,
                new ProfileView(user));
    }

    @PostMapping(path = "/public/signup")
    public ResponseEntity<Object> signup(@RequestBody SignupUserView userSignup) {

        Optional<User> userSearch = userRepository.findFirstByEmail(userSignup.getEmail());
        if (userSearch.isPresent()) {
            return ResponseHandler.generateResponse("Error: Email is already taken!", HttpStatus.BAD_REQUEST, null);
        }
        User user = new User();
        user.setFirstName(userSignup.getFirstName());
        user.setLastName(userSignup.getLastName());
        user.setEmail(userSignup.getEmail()); 
        user.setBirthDate(userSignup.getBirthDate());
        user.setPhone(userSignup.getPhone());
        List<String> roles = Arrays.asList("USER");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userSignup.getPassword()));
        userRepository.save(user);

        return ResponseHandler.generateResponse("User registered successfully!", HttpStatus.OK, new ProfileView(user));
    }
}
