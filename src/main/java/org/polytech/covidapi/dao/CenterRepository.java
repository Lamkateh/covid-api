package org.polytech.covidapi.dao;

import java.util.List;

import org.polytech.covidapi.entities.Center;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Integer> {

    List<Center> findAllCentersByCity(String city);
    List<Center> findAllCentersByName(String name);
    List<Center> findAllByOrderByIdAsc();

}
