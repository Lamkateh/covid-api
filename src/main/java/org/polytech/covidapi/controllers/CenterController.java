package org.polytech.covidapi.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CenterController {
    
    
    private final CenterRepository centerRepository;

    CenterController(CenterRepository centerRepository){
        this.centerRepository = centerRepository;
    }

    @GetMapping(path = "/public/centers/city/{city}")
    List<Center> findAllCentersByCity(@PathVariable String city) {    // TO DO : like 
        return centerRepository.findAllCentersByCity(city);
    }

    @GetMapping(path = "/public/centers/name/{name}")
    List<Center> findAllCentersByName(@PathVariable String name) {    // TO DO : like 
        return centerRepository.findAllCentersByName(name);
    }

    @GetMapping(path = "/public/centers")
    List<Center> findAllCenters() {    // TO DO : like
        return centerRepository.findAllByOrderByIdAsc();
    }

    @GetMapping(path = "/public/center/{id}")
    Center findCenterById(@PathVariable("id") int id) {
        return centerRepository.findFirstById(id);
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
