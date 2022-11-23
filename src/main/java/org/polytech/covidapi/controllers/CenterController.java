package org.polytech.covidapi.controllers;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.response.ResponseHandler;
import org.polytech.covidapi.services.Base64Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CenterController {

    @Autowired
    private final CenterRepository centerRepository;

    public CenterController(CenterRepository centerRepository) {
        this.centerRepository = centerRepository;
    }

    @GetMapping(path = "/public/centers/city/{city}")
    public ResponseEntity<Object> findAllCentersByCity(@PathVariable String city, @PageableDefault(size = 15) Pageable p) { 
        city = Base64Service.decode(city);
        return ResponseHandler.generateResponse("Centers successfully retrieved", HttpStatus.OK, centerRepository.findAllCentersByCityContainingIgnoreCase(city, p));
    }

    @GetMapping(path = "/public/centers/name/{name}")
    public ResponseEntity<Object> findAllCentersByName(@PathVariable String name, @PageableDefault(size = 15) Pageable p) {
        name = Base64Service.decode(name);
        return ResponseHandler.generateResponse("Centers successfully retrieved", HttpStatus.OK, centerRepository.findAllCentersByNameContainingIgnoreCase(name, p));
    }

    @GetMapping(path = "/public/centers")
    public ResponseEntity<Object> findAllCenters(@PageableDefault(size = 15) Pageable p) {
        return ResponseHandler.generateResponse("Centers successfully retrieved", HttpStatus.OK, centerRepository.findAllByOrderByCityAsc(p));
    }

    @GetMapping(path = "/public/centers/{id}")
    public ResponseEntity<Object> findCenterById(@PathVariable("id") int id) {
        return ResponseHandler.generateResponse("Center successfully retrieved", HttpStatus.OK, centerRepository.findFirstById(id));
    }

    /*
     * @PostMapping("/admin/center")
     * Center store(@RequestBody Center center){
     * // Récupère données user connectée A VOIR AVEC COURS JWT
     * // checker rôle
     * Center centerSaved = new Center(center.getName(), center.getCity(),
     * center.getZipCode(), center.getAddress(), center.getPhone(),
     * center.getEmail());
     * centerRepository.save(centerSaved);
     * return centerRepository.findFirstById(centerSaved.getId());
     * }
     */

}
