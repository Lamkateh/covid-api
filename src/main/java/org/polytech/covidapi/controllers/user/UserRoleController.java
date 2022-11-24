package org.polytech.covidapi.controllers.user;

import java.util.ArrayList;
import java.util.List;

import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.Role;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.exception.ResourceNotFoundException;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRoleController {
    @Autowired
    private UserRepository userRepository;

    public UserRoleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/private/users/{id}/role")
    public ResponseEntity<Object> updateRole(@PathVariable("id") int id, @RequestBody Role role) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<String> roles = new ArrayList<String>();
        roles.add(role.getName().toUpperCase());
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseHandler.generateResponse("Role successfully updated", HttpStatus.OK, user);
    }
}