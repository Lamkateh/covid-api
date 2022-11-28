package org.polytech.covidapi.controllers.user;

import java.time.LocalDate;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.ERole;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.exception.ResourceNotFoundException;
import org.polytech.covidapi.facade.IAuthenticationFacade;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private final CenterRepository centerRepository;

    public UserController(UserRepository userRepository, CenterRepository centerRepository) {
        this.userRepository = userRepository;
        this.centerRepository = centerRepository;
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

    @PostMapping(path = "/public/centers/{id}/users")
    public ResponseEntity<Object> storeUser(@PathVariable("id") int id, @RequestBody User user)
            throws ResourceNotFoundException {
       /* if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.SUPER_ADMIN)
                && !authenticationFacade.hasRole(ERole.DOCTOR)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }*/

        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));

        userRepository.save(user);

        center.addDoctor(user);
        centerRepository.save(center);
        return ResponseHandler.generateResponse("User successfully created and registred to the center", HttpStatus.OK,
                user);
    }

}
