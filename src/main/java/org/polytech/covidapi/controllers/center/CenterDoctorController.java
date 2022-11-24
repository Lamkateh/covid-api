package org.polytech.covidapi.controllers.center;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.exception.ResourceNotFoundException;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CenterDoctorController {
    @Autowired
    private final CenterRepository centerRepository;

    @Autowired
    private final UserRepository userRepository;

    public CenterDoctorController(CenterRepository centerRepository, UserRepository userRepository) {
        this.centerRepository = centerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/private/centers/{id}/doctors")
    public ResponseEntity<Object> findAllDoctorsByCenterId(@PathVariable("id") int id) throws ResourceNotFoundException {
        Center center = centerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        return ResponseHandler.generateResponse("Doctors successfully retrieved", HttpStatus.OK, center.getDoctors());
    }

    @PostMapping(path = "/private/centers/{id}/doctors")
    public ResponseEntity<Object> addDoctorToCenter(@PathVariable("id") int id, @RequestBody User doctorDetails) throws ResourceNotFoundException {
        Center center = centerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        User doctor = userRepository.findById(doctorDetails.getId()).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        center.addDoctor(doctor);
        return ResponseHandler.generateResponse("Doctor successfully added to center", HttpStatus.OK, center);
    }

    @DeleteMapping(path = "/private/centers/{id}/doctors/{doctorId}")
    public ResponseEntity<Object> removeDoctorFromCenter(@PathVariable("id") int id, @PathVariable("doctorId") int doctorId) throws ResourceNotFoundException {
        Center center = centerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        User doctor = userRepository.findById(doctorId).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        center.removeDoctor(doctor);
        return ResponseHandler.generateResponse("Doctor successfully removed from center", HttpStatus.OK, center);
    }
}
