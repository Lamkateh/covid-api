package org.polytech.covidapi.controllers.center;

import java.time.Duration;

import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dto.center.CenterCreationView;
import org.polytech.covidapi.dto.center.CenterPreviewView;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.ERole;
import org.polytech.covidapi.exception.ResourceNotFoundException;
import org.polytech.covidapi.facade.IAuthenticationFacade;
import org.polytech.covidapi.response.ResponseHandler;
import org.polytech.covidapi.services.Base64Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import io.github.bucket4j.*;

@RestController
public class CenterController {

    // rajoute 10 tokens toutes les minutes
    Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
    // capacité max de 10 token
    Bandwidth limit = Bandwidth.classic(10, refill);
    Bucket bucket = Bucket.builder().addLimit(limit).build();

    @Autowired
    private final CenterRepository centerRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    public CenterController(CenterRepository centerRepository) {
        this.centerRepository = centerRepository;
    }

    @GetMapping(path = "/public/centers/city/{city}")
    public ResponseEntity<Object> findAllCentersByCity(@PathVariable String city,
            @PageableDefault(size = 16) Pageable p) {
        city = Base64Service.decode(city);
        Page<Center> page = centerRepository.findAllCentersByCityContainingIgnoreCase(city, p);
            return ResponseHandler.generateResponse("Centers successfully retrieved", HttpStatus.OK,
                    new PageImpl<CenterPreviewView>(
                            CenterPreviewView.convert(page.getContent()), p, page.getTotalElements()));
    }

    @GetMapping(path = "/public/centers/name/{name}")
    public ResponseEntity<Object> findAllCentersByName(@PathVariable String name,
            @PageableDefault(size = 16) Pageable p) {
        name = Base64Service.decode(name);
        Page<Center> page = centerRepository.findAllCentersByNameContainingIgnoreCase(name, p);
            return ResponseHandler.generateResponse("Centers successfully retrieved", HttpStatus.OK,
                    new PageImpl<CenterPreviewView>(
                            CenterPreviewView.convert(page.getContent()), p, page.getTotalElements()));
    }

    @GetMapping(path = "/public/centers")
    public ResponseEntity<Object> findAllCenters(@PageableDefault(size = 16) Pageable p) {

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            Page<Center> page = centerRepository.findAllByOrderByCityAsc(p);
            return ResponseHandler.generateResponse("Centers successfully retrieved", HttpStatus.OK,
                    new PageImpl<CenterPreviewView>(
                            CenterPreviewView.convert(page.getContent()), p, page.getTotalElements()));
        }

        long delaiEnSeconde = probe.getNanosToWaitForRefill() / 1_000_000_000;
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-Rate-Limit-Retry-After-Seconds", String.valueOf(delaiEnSeconde))
                .build();
    }

    @GetMapping(path = "/public/centers/{id}")
    public ResponseEntity<Object> findCenterById(@PathVariable("id") int id) throws ResourceNotFoundException {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        CenterPreviewView centerPreview = new CenterPreviewView(center);
        return ResponseHandler.generateResponse("Center successfully retrieved", HttpStatus.OK, centerPreview);
    }

    @PostMapping(path = "/private/centers")
    public ResponseEntity<Object> store(@RequestBody Center center) {

        if (!authenticationFacade.hasRole(ERole.SUPERADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource",
                    HttpStatus.FORBIDDEN,
                    null);
        }

        if (center.getName() == null || center.getName().isEmpty()) {
            return ResponseHandler.generateResponse("Name is required", HttpStatus.BAD_REQUEST, null);
        }

        center = centerRepository.save(center);
        CenterCreationView centerPreview = new CenterCreationView(center);
        return ResponseHandler.generateResponse("Center successfully stored", HttpStatus.OK, centerPreview);
    }

    @PutMapping(path = "/private/centers/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Center centerDetails)
            throws ResourceNotFoundException {
        if (!authenticationFacade.hasRole(ERole.SUPERADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));

        if (centerDetails.getName() == null || centerDetails.getName().isEmpty()) {
            return ResponseHandler.generateResponse("Name is required", HttpStatus.BAD_REQUEST, null);
        }

        center.setName(centerDetails.getName());
        if (centerDetails.getCity() != null && !centerDetails.getCity().isEmpty()) {
            center.setCity(centerDetails.getCity());
        }
        if (centerDetails.getAddress() != null && !centerDetails.getAddress().isEmpty()) {
            center.setAddress(centerDetails.getAddress());
        }
        if (centerDetails.getPhone() != null && !centerDetails.getPhone().isEmpty()) {
            center.setPhone(centerDetails.getPhone());
        }
        if (centerDetails.getZipCode() != null && !centerDetails.getZipCode().isEmpty()) {
            center.setZipCode(centerDetails.getZipCode());
        }
        if (centerDetails.getEmail() != null && !centerDetails.getEmail().isEmpty()) {
            center.setEmail(centerDetails.getEmail());
        }
        Center updatedCenter = centerRepository.save(center);
        return ResponseHandler.generateResponse("Center successfully updated", HttpStatus.OK, updatedCenter);
    }

    @DeleteMapping(path = "/private/centers/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        if (!authenticationFacade.hasRole(ERole.SUPERADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        centerRepository.deleteById(id);
        return ResponseHandler.generateResponse("Center successfully deleted", HttpStatus.OK, null);
    }
}