package org.polytech.covidapi.dao;

import java.time.LocalDate;
import java.util.List;

import org.polytech.covidapi.entities.Appointment;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // @Query("Select c from Center c join fetch c.doctors where c.city = :city")
    List<Appointment> findAppointmentsByCenterAndDate(Center center, LocalDate appointmentDate);

    List<Appointment> findAppointmentsByDoctorOrderByDateAsc(User doctor);

    Appointment findFirstById(int id);

}
