package org.polytech.covidapi.controllers.user;

import java.util.ArrayList;
import java.util.Optional;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.dto.ProfileView;
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
import org.springframework.web.bind.annotation.*;

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
                userRepository.findAllByOrderByLastName());
    }

    @GetMapping(path = "/private/users/{id}")
    public ResponseEntity<Object> show(@PathVariable("id") int id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseHandler.generateResponse("User successfully retrieved", HttpStatus.OK, user);
    }

    @GetMapping(path = "/private/superAdmins")
    public ResponseEntity<Object> superAdmins() {
        return ResponseHandler.generateResponse("Super admins successfully retrieved", HttpStatus.OK,
                userRepository.findByRoles("SUPER_ADMIN"));
    }

    @PostMapping(path = "/private/users")
    public ResponseEntity<Object> store(@RequestBody SignupUserView userSignup) throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.SUPER_ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource",
                    HttpStatus.FORBIDDEN,
                    null);
        }

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
        user.setPassword(passwordEncoder.encode(userSignup.getPassword()));
        userRepository.save(user);
        if (userSignup.getCenterId() != null) {
            Center center = centerRepository.findById(userSignup.getCenterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
            if (userSignup.getRoles().get(0).equals("DOCTOR")) {
                user.setCenter(center);
                center.addDoctor(user);
            } else if (userSignup.getRoles().get(0).equals("ADMIN")) {
                user.setCenter(center);
                center.addAdmin(user);
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

        userRepository.save(user);
        ProfileView userProfileView = new ProfileView(user);
        return ResponseHandler.generateResponse("User successfully created", HttpStatus.CREATED,
                userProfileView);
    }

    @PutMapping(path = "/private/users/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody SignupUserView userDetails)
            throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // TODO : better validation
        if (userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty()) {
            return ResponseHandler.generateResponse("First name is required", HttpStatus.BAD_REQUEST, null);
        }
        if (userDetails.getLastName() == null || userDetails.getLastName().isEmpty()) {
            return ResponseHandler.generateResponse("Last name is required", HttpStatus.BAD_REQUEST, null);
        }
        if (userDetails.getEmail() == null || userDetails.getEmail().isEmpty()) {
            return ResponseHandler.generateResponse("Email is required", HttpStatus.BAD_REQUEST, null);
        }
        if (userDetails.getPhone() == null || userDetails.getPhone().isEmpty()) {
            return ResponseHandler.generateResponse("Phone is required", HttpStatus.BAD_REQUEST, null);
        }
        if (userDetails.getBirthDate() == null || userDetails.getBirthDate().toString().isEmpty()) {
            return ResponseHandler.generateResponse("Birth date is required", HttpStatus.BAD_REQUEST, null);
        }
        if (userDetails.getRoles() == null || userDetails.getRoles().isEmpty()) {
            return ResponseHandler.generateResponse("Role is required", HttpStatus.BAD_REQUEST, null);
        }
        if (!userDetails.getRoles().get(0).equals("SUPERADMIN")
                && (userDetails.getCenterId() == null || userDetails.getCenterId().toString().isEmpty())) {
            return ResponseHandler.generateResponse("Center id is required", HttpStatus.BAD_REQUEST, null);
        }

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setBirthDate(userDetails.getBirthDate());
        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setPhone(userDetails.getPhone());
        user.setDisabled(userDetails.getDisabled());
        user.setRoles(userDetails.getRoles());
        if (!userDetails.getRoles().get(0).equals("SUPERADMIN")) {
            Center center = centerRepository.findById(userDetails.getCenterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
            user.setCenter(center);
        }

        Optional<ProfileView> updatedUser = Optional.of(new ProfileView(userRepository.save(user))); // TODO fonctionne
                                                                                                     // mais à vérifier
                                                                                                     // si ça génère pas
                                                                                                     // de bug ailleurs
        return ResponseHandler.generateResponse("User successfully updated", HttpStatus.OK, updatedUser.get());
    }

    @DeleteMapping(path = "/private/users/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        if (!authenticationFacade.hasRole(ERole.ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        userRepository.deleteById(id);
        return ResponseHandler.generateResponse("User successfully deleted", HttpStatus.OK, null);
    }
}