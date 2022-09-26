package org.polytech.covidapi.controllers;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.entities.Center;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CenterController {
    
    @Autowired
    private final CenterRepository centerRepository;

    CenterController(CenterRepository centerRepository){
        this.centerRepository = centerRepository;
    }

    @GetMapping("/public/centers/{city}")
    Page<Center> findAllByCity(@PathVariable String city, @PageableDefault(size = 15) Pageable p) {
        return centerRepository.findAllByCity(city, p);
    }

    @GetMapping("/public/centers/{name}")
    Page<Center> findAllByName(@PathVariable String name, @PageableDefault(size = 15) Pageable p) {
        return centerRepository.findAllByCity(name, p);
    }

    /* 
    @PostMapping("/admin/center")
    Center store(@RequestBody Center center){
        // Récupère données user connectée   A VOIR AVEC COURS JWT
        // checker rôle
        Center centerSaved = new Center(center.getName(), center.getCity(), center.getZipCode(), center.getAddress(), center.getPhone(), center.getEmail());
        centerRepository.save(centerSaved);
        return centerRepository.findFirstById(centerSaved.getId());
    }*/




}
