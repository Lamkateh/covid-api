package org.polytech.covidapi.controllers.center;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.exception.ResourceNotFoundException;
import org.polytech.covidapi.response.ResponseHandler;
import org.polytech.covidapi.services.Base64Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Object> findCenterById(@PathVariable("id") int id) throws ResourceNotFoundException {
        Center center = centerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        return ResponseHandler.generateResponse("Center successfully retrieved", HttpStatus.OK, center);
    }

    @PostMapping(path = "/private/centers")
    public ResponseEntity<Object> store(@RequestBody Center center) {
        //TODO : check user permissions

        // TODO : better validation
        if (center.getName() == null || center.getName().isEmpty()) {
            return ResponseHandler.generateResponse("Name is required", HttpStatus.BAD_REQUEST, null);
        }

        center = centerRepository.save(center);
        return ResponseHandler.generateResponse("Center successfully stored", HttpStatus.OK, center);
    }

    @PutMapping(path = "/private/centers/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Center centerDetails) throws ResourceNotFoundException {
        //TODO : check user permissions
        Center center = centerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Center not found"));

        // TODO : better validation
        if (centerDetails.getName() == null || centerDetails.getName().isEmpty()) {
            return ResponseHandler.generateResponse("Name is required", HttpStatus.BAD_REQUEST, null);
        }
        if (centerDetails.getAddress() == null || centerDetails.getAddress().isEmpty()) {
            return ResponseHandler.generateResponse("Address is required", HttpStatus.BAD_REQUEST, null);
        }
        if (centerDetails.getCity() == null || centerDetails.getCity().isEmpty()) {
            return ResponseHandler.generateResponse("City is required", HttpStatus.BAD_REQUEST, null);
        }
        if (centerDetails.getZipCode() == null || centerDetails.getZipCode().isEmpty()) {
            return ResponseHandler.generateResponse("ZipCode is required", HttpStatus.BAD_REQUEST, null);
        }

        center.setName(centerDetails.getName());
        center.setCity(centerDetails.getCity());
        center.setAddress(centerDetails.getAddress());
        center.setPhone(centerDetails.getPhone());
        center.setZipCode(centerDetails.getZipCode());
        center.setEmail(centerDetails.getEmail());
        Center updatedCenter = centerRepository.save(center);
        return ResponseHandler.generateResponse("Center successfully updated", HttpStatus.OK, updatedCenter);
    }

    @DeleteMapping(path = "/private/centers/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        //TODO : check user permissions
        centerRepository.deleteById(id);
        return ResponseHandler.generateResponse("Center successfully deleted", HttpStatus.OK, null);
    }
}
