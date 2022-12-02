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
public class CenterDoctorController {
    @Autowired
    private final CenterRepository centerRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public CenterDoctorController(CenterRepository centerRepository, UserRepository userRepository) {
        this.centerRepository = centerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/private/centers/{id}/doctors")
    public ResponseEntity<Object> findAllDoctorsByCenterId(@PathVariable("id") int id)
            throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.SUPER_ADMIN)
                && !authenticationFacade.hasRole(ERole.DOCTOR)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        return ResponseHandler.generateResponse("Doctors successfully retrieved", HttpStatus.OK, ProfileView.convert(center.getDoctors()));
    }

    @PostMapping(path = "/private/centers/{id}/doctors")
    public ResponseEntity<Object> addDoctorToCenter(@PathVariable("id") int id, @RequestBody User doctorDetails)
            throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.SUPER_ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        User doctor = userRepository.findById(doctorDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        List<String> roles = new ArrayList<String>();
        roles.add("DOCTOR");
        doctor.setRoles(roles);
        userRepository.save(doctor);
        center.addDoctor(doctor);
        centerRepository.save(center);
        return ResponseHandler.generateResponse("Doctor successfully added to center", HttpStatus.OK, center);
    }

    @DeleteMapping(path = "/private/centers/{id}/doctors/{doctorId}")
    public ResponseEntity<Object> removeDoctorFromCenter(@PathVariable("id") int id,
            @PathVariable("doctorId") int doctorId) throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.SUPER_ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        List<String> roles = new ArrayList<String>();
        roles.add("USER");
        doctor.setRoles(roles);
        userRepository.save(doctor);
        center.removeDoctor(doctor);
        centerRepository.save(center);
        return ResponseHandler.generateResponse("Doctor successfully removed from center", HttpStatus.OK, center);
    }
}
