package org.polytech.covidapi.controllers;

import liquibase.repackaged.org.apache.commons.text.WordUtils;
import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.entities.Center;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CenterController {

    @Autowired
    private final CenterRepository centerRepository;

    CenterController(CenterRepository centerRepository) {
        this.centerRepository = centerRepository;
    }

    @GetMapping(path = "/public/centers/city/{city}")
    Page<Center> findAllCentersByCity(@PathVariable String city, @PageableDefault(size = 15) Pageable p) { // TO DO :
                                                                                                           // like
        city = WordUtils.capitalizeFully(city);
        return centerRepository.findAllCentersByCity(city, p);
    }

    @GetMapping(path = "/public/centers/name/{name}")
    Page<Center> findAllCentersByName(@PathVariable String name, @PageableDefault(size = 15) Pageable p) { // TO DO :
                                                                                                           // like
        name = WordUtils.capitalizeFully(name);
        return centerRepository.findAllCentersByName(name, p);
    }

    @GetMapping(path = "/public/centers")
    Page<Center> findAllCenters(@PageableDefault(size = 15) Pageable p) { // TO DO : like
        return centerRepository.findAllByOrderByCityAsc(p);
    }

    @GetMapping(path = "/public/center/{id}")
    Center findCenterById(@PathVariable("id") int id) {
        return centerRepository.findFirstById(id);
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
