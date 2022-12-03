package org.polytech.covidapi.dao;

import java.util.List;
import java.util.Optional;

import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByFirstNameOrLastName(String firstname, String lastname);
    List<User> findByFirstNameOrLastName(String firstname, String lastname);
    List<User> findByCenter(Center center);
    List<User> findAll();
    User findFirstById(int id);
    Optional<User> findFirstByEmail(String email);
    List<User> findUsersByCenter(Center center);
    User findFirstByEmailAndPassword(String email, String password);
}
