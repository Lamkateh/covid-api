package org.polytech.covidapi.dao;

import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByFirstNameOrLastName(String firstname, String lastname);
    Page<User> findByFirstNameOrLastName(String firstname, String lastname, Pageable p);
    Page<User> findByCenter(Center center, Pageable p);
    Page<User> findAll(Pageable p);
}
