package org.polytech.covidapi.dao;

import java.util.Date;
import java.util.List;

import org.polytech.covidapi.entities.Appointment;
import org.polytech.covidapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

    

}
