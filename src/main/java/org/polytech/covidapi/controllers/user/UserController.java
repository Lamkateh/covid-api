package org.polytech.covidapi.controllers.user;

import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/private/users")
    public ResponseEntity<Object> index() {
        return ResponseHandler.generateResponse("Users successfully retrieved", HttpStatus.OK, userRepository.findAll());
    }

    @GetMapping(path = "/private/users/{id}")
    public ResponseEntity<Object> show(@PathVariable("id") int id) {
        return ResponseHandler.generateResponse("User successfully retrieved", HttpStatus.OK, userRepository.findFirstById(id));
    }
}
