package org.polytech.covidapi.controllers;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.polytech.covidapi.dao.AppointmentRepository;
import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.dto.AppointmentsCenterView;
import org.polytech.covidapi.dto.DayView;
import org.polytech.covidapi.dto.appointment.AppointmentAdminView;
import org.polytech.covidapi.dto.appointment.AppointmentDoctorView;
import org.polytech.covidapi.dto.appointment.AppointmentPreviewView;
import org.polytech.covidapi.dto.appointment.AppointmentView;
import org.polytech.covidapi.entities.Appointment;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.ERole;
import org.polytech.covidapi.entities.User;
import org.polytech.covidapi.facade.IAuthenticationFacade;
import org.polytech.covidapi.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CenterRepository centerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private MeterRegistry meterRegistry;

    @GetMapping(path = "/public/centers/{id}/appointments")
    public ResponseEntity<Object> findAllAppointmentsAvailableByCenterId(@PathVariable("id") int center_id) {

        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        List<DayView> days = new ArrayList<DayView>();
        LocalTime time = nowTime.minusSeconds(LocalTime.now().getSecond())
                .minusNanos(LocalTime.now().getNano());
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime closeTime = LocalTime.of(18, 0);
        Center center = centerRepository.findFirstById(center_id);

        int doctorCount = center.getDoctors().size();

        for (int day = 0; day < 7; day++) {

            List<AppointmentPreviewView> appointmentsAvailable = new ArrayList<AppointmentPreviewView>();
            LocalDate newDate = nowDate.plusDays(day);
            List<Appointment> appointmentsUnavailable = appointmentRepository.findAppointmentsByCenterAndDate(center,
                    newDate);
            List<LocalTime> timeUnavailable = new ArrayList<LocalTime>();
            if (doctorCount > 0) {
                for (Appointment a : appointmentsUnavailable) {
                    timeUnavailable.add(a.getTime());
                }

                if (day == 0) {
                    int unroundedMinutes = time.getMinute();
                    int mod = unroundedMinutes % 15;
                    LocalTime timeRounded = time.plusMinutes((15 - mod));
                    while (timeRounded.isBefore(closeTime)) {
                        if (!timeUnavailable.contains(timeRounded) && !timeRounded.isBefore(startTime)) {
                            AppointmentPreviewView appointment = new AppointmentPreviewView(timeRounded,
                                    center.getId());
                            appointmentsAvailable.add(appointment);
                        }
                        timeRounded = timeRounded.plusMinutes(15);
                    }
                } else {
                    LocalTime baseTime = LocalTime.of(8, 0); // 8h
                    while (baseTime.isBefore(closeTime)) // Jusqu'a 18h
                    {
                        if (!timeUnavailable.contains(baseTime)) {
                            AppointmentPreviewView appointment = new AppointmentPreviewView(baseTime, center.getId());
                            appointmentsAvailable.add(appointment);
                        }
                        baseTime = baseTime.plusMinutes(15);
                    }
                }
            }
            DayView dayView = new DayView(newDate, appointmentsAvailable);
            days.add(dayView);
        }
        return ResponseHandler.generateResponse("Appointment successfully retrieved", HttpStatus.OK,
                new AppointmentsCenterView(days, startTime, closeTime));
    }

    @GetMapping(path = "/private/admins/{id}/appointments")
    public ResponseEntity<Object> findAllAppointmentsByAdmin(@PathVariable("id") int admin_id) {

        if (!authenticationFacade.hasRole(ERole.ADMIN)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        User admin = null;
        try {
            admin = userRepository.findFirstById(admin_id);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Admin not found", HttpStatus.NOT_FOUND, null);
        }

        List<Appointment> appointments = appointmentRepository
                .findAppointmentsByCenterOrderByDateAsc(admin.getCenter());

        return ResponseHandler.generateResponse("Appointment successfully retrieved", HttpStatus.OK,
                AppointmentAdminView.convert(appointments));
    }

    @GetMapping(path = "/private/doctors/{id}/appointments")
    public ResponseEntity<Object> findAllAppointmentsByDoctor(@PathVariable("id") int doctor_id) {

        if (!authenticationFacade.hasRole(ERole.DOCTOR)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        User doctor = null;
        try {
            doctor = userRepository.findFirstById(doctor_id);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Doctor not found", HttpStatus.NOT_FOUND, null);
        }

        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorOrderByDateAsc(doctor);

        return ResponseHandler.generateResponse("Appointment successfully retrieved", HttpStatus.OK,
                AppointmentDoctorView.convert(appointments));

    }

    @PostMapping(path = "/private/centers/{id}/appointments")
    public ResponseEntity<Object> registerAppointment(@PathVariable("id") int center_id,
            @RequestParam("patient_id") String patient_id,
            @RequestParam("time") String time,
            @RequestParam("date") String date) {
        Center center = null;
        try {
            center = centerRepository.findFirstById(center_id);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseHandler.generateResponse("Center not found", HttpStatus.NOT_FOUND, null);
        }

        // parse int patient_id
        int patient_id_int = 0;
        try {
            patient_id_int = Integer.parseInt(patient_id);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Patient not found", HttpStatus.NOT_FOUND, null);
        }

        User patient = null;
        try {
            patient = userRepository.findFirstById(patient_id_int);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseHandler.generateResponse("Patient not found", HttpStatus.NOT_FOUND, null);
        }

        int doctorsNumber = center.getDoctors().size();
        if (doctorsNumber == 0) {
            return ResponseHandler.generateResponse("Doctor not found", HttpStatus.NOT_FOUND, null);
        }

        // Get random doctor
        User doctor = center.getDoctors().get((int) (Math.random() * doctorsNumber));

        LocalTime timeObj = LocalTime.parse(time);
        LocalDate dateObj = LocalDate.parse(date);

        Appointment appointment = new Appointment(dateObj, timeObj, false, center,
                patient, doctor);

        appointmentRepository.save(appointment);
        meterRegistry.counter("appointment.registered").increment();
        return ResponseHandler.generateResponse("Appointment successfully registered", HttpStatus.OK,
                new AppointmentView(appointment.getTime(), appointment.getDate(),
                        appointment.getCenter().getId()));
    }

    @PutMapping(path = "/private/appointments/{id}")
    public ResponseEntity<Object> AppointmentDone(@PathVariable("id") int appointment_id) {

        if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.DOCTOR)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }

        Appointment appointment = null;
        try {
            appointment = appointmentRepository.findFirstById(appointment_id);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Appointment not found", HttpStatus.NOT_FOUND, null);
        }

        appointment.setIsDone(true);

        appointmentRepository.save(appointment);

        return ResponseHandler.generateResponse("Appointment successfully updated", HttpStatus.OK,
                new AppointmentView(appointment.getTime(), appointment.getDate(),
                        appointment.getCenter().getId()));
    }

    @DeleteMapping(path = "/private/appointments/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable int id) {
        if (!authenticationFacade.hasRole(ERole.ADMIN) && !authenticationFacade.hasRole(ERole.DOCTOR)) {
            return ResponseHandler.generateResponse("You are not allowed to access this resource", HttpStatus.FORBIDDEN,
                    null);
        }
        appointmentRepository.deleteById(id);
        meterRegistry.counter("appointment.deleted").increment(-1);
        return ResponseHandler.generateResponse("Appointment successfully deleted", HttpStatus.OK, null);
    }
}
