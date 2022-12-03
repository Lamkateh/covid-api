package org.polytech.covidapi.dao;

import java.util.List;
import org.polytech.covidapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findByName(String name);
}
