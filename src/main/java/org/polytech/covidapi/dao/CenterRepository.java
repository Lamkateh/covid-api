package org.polytech.covidapi.dao;

import java.util.List;

import org.polytech.covidapi.entities.Center;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Integer> {

    Page<Center> findAllCentersByCity(String city, Pageable p);
    Page<Center> findAllCentersByName(String name, Pageable p);
    Page<Center> findAllByOrderByCityAsc(Pageable p);
    Center findFirstById(int id);

}
