package org.polytech.covidapi.controllers.user;

import java.io.Console;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.dto.SignupUserView;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.ERole;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.exception.ResourceNotFoundException;
import org.polytech.covidapi.facade.IAuthenticationFacade;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private final CenterRepository centerRepository;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, CenterRepository centerRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.centerRepository = centerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/private/users")
    public ResponseEntity<Object> index() {
        return ResponseHandler.generateResponse("Users successfully retrieved", HttpStatus.OK,
                userRepository.findAll());
    }

    @GetMapping(path = "/private/users/{id}")
    public ResponseEntity<Object> show(@PathVariable("id") int id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseHandler.generateResponse("User successfully retrieved", HttpStatus.OK, user);
    }

    @PostMapping(path = "/private/users")
    public ResponseEntity<Object> store(@RequestBody SignupUserView userSignup) throws ResourceNotFoundException {
        /*
         * if (!authenticationFacade.hasRole(ERole.ADMIN) &&
         * !authenticationFacade.hasRole(ERole.SUPER_ADMIN)) {
         * return ResponseHandler.
         * generateResponse("You are not allowed to access this resource",
         * HttpStatus.FORBIDDEN,
         * null);
         * }
         */

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
        System.out.println(userSignup.getCenter_id());
        if (userSignup.getCenter_id() != null) {
            System.out.println("test");
            Center center = centerRepository.findById(userSignup.getCenter_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
            System.out.println(userSignup.getRoles().get(0));
            if (userSignup.getRoles().get(0).equals("DOCTOR")) {
                System.out.println("test");
                user.setCenter(center);
                center.addDoctor(user);
            }
            centerRepository.save(center);

        }
        if (userSignup.getRoles() != null) {
            user.setRoles(userSignup.getRoles());
        } else {
            ArrayList<String> roles = new ArrayList<String>();
            roles.add("USER");
            user.setRoles(roles);
        }

        user.setPassword(passwordEncoder.encode(userSignup.getPassword()));
        userRepository.save(user);

        return ResponseHandler.generateResponse("User successfully created", HttpStatus.CREATED,
                userRepository.save(user));
    }

}
