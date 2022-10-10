package org.polytech.covidapi.controllers;

import java.util.ArrayList;
import java.util.List;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoctorsController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CenterRepository centerRepository;

    public DoctorsController(UserRepository userRepository, CenterRepository centerRepository) {
        this.userRepository = userRepository;
        this.centerRepository = centerRepository;
    }

    @GetMapping(path = "/public/admin/center/{id}/doctors")
    List<User> findAllDoctorsByCenter(@PathVariable("id") int center_id) {
        List<User> doctors = new ArrayList<User>();
        Center center = centerRepository.findFirstById(center_id);
        doctors = center.getDoctors();
        return doctors;
    }
}
