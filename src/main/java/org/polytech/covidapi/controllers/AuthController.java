package org.polytech.covidapi.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.security.MessageResponse;
import org.polytech.covidapi.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

import io.micrometer.core.ipc.http.HttpSender.Response;

@RestController
public class AuthController {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserServices userService;
    @Autowired
    AuthenticationManager authenticationManager;

    AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/private/me")
    public Optional<User> getUserDetails() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = details.getUsername();
        return userRepository.findFirstByEmail(userEmail);
    }

    @PostMapping(path = "/public/signin")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error :Incorrect username or password. "));
        }
    }

    @PostMapping(path = "/public/signup")
    public ResponseEntity<?> signup(@RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name,
            @RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "birth_date", required = false) LocalDate birthDate) {

        Optional<User> userSearch = userRepository.findFirstByEmail(email);
        if (userSearch.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setPhone(phone);
        List<String> roles = Arrays.asList("USER");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path = "/private/superAdmin/signup/admin")
    public ResponseEntity<?> signupAdmin(@RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name,
            @RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "birth_date", required = false) LocalDate birthDate) {
        Optional<User> userSearch = userRepository.findFirstByEmail(email);
        if (userSearch.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setPhone(phone);
        List<String> roles = Arrays.asList("ADMIN");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path = "/private/admin/signup/doctors")
    public ResponseEntity<?> signupDoctors(@RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name,
            @RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "birth_date", required = false) LocalDate birthDate) {
        Optional<User> userSearch = userRepository.findFirstByEmail(email);
        if (userSearch.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setPhone(phone);
        List<String> roles = Arrays.asList("DOCTORS");
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().body(user);
    }

}
