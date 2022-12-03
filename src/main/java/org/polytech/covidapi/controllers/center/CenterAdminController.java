package org.polytech.covidapi.controllers.center;

import java.util.ArrayList;
import java.util.List;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.dto.ProfileView;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.ERole;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.exception.ResourceNotFoundException;
import org.polytech.covidapi.facade.IAuthenticationFacade;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CenterAdminController {

    @Autowired
    private final CenterRepository centerRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public CenterAdminController(CenterRepository centerRepository, UserRepository userRepository) {
        this.centerRepository = centerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/private/centers/{id}/admins")
    public ResponseEntity<Object> findAllAdminsByCenterId(@PathVariable("id") int id)
            throws ResourceNotFoundException {
        // TODO Enlever ADMIN
        if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.SUPER_ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        return ResponseHandler.generateResponse("Admins successfully retrieved", HttpStatus.OK, ProfileView.convert(center.getAdmins()));
    }

    @PostMapping(path = "/private/centers/{id}/admins")
    public ResponseEntity<Object> addAdminToCenter(@PathVariable("id") int id, @RequestBody User adminDetails)
            throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.SUPER_ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        User admin = userRepository.findById(adminDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        List<String> roles = new ArrayList<String>();
        roles.add("ADMIN");
        admin.setRoles(roles);
        userRepository.save(admin);
        center.addAdmin(admin);
        centerRepository.save(center);
        return ResponseHandler.generateResponse("Admin successfully added to center", HttpStatus.OK, center);
    }

    @DeleteMapping(path = "/private/centers/{id}/admins/{adminId}")
    public ResponseEntity<Object> removeAdminFromCenter(@PathVariable("id") int id,
            @PathVariable("adminId") int adminId) throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.SUPER_ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        List<String> roles = new ArrayList<String>();
        roles.add("USER");
        admin.setRoles(roles);
        userRepository.save(admin);
        center.removeAdmin(admin);
        centerRepository.save(center);
        return ResponseHandler.generateResponse("Admin successfully removed from center", HttpStatus.OK, center);
    }
}