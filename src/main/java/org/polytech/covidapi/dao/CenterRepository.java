package org.polytech.covidapi.dao;

import org.polytech.covidapi.entities.Center;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Integer> {

    //@Query("Select c from Center c join fetch c.doctors where c.city = :city")
    Page<Center> findAllCentersByCityContainingIgnoreCase(String city, Pageable p);
    Page<Center> findAllCentersByNameContainingIgnoreCase(String name, Pageable p);
    Page<Center> findAllByOrderByCityAsc(Pageable p);
    Center findFirstById(int id);

}
