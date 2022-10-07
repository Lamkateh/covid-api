package org.polytech.covidapi.controllers;

import java.time.LocalTime;
import java.util.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.polytech.covidapi.dao.AppointmentRepository;
import org.polytech.covidapi.dao.CenterRepository;
import org.polytech.covidapi.dao.UserRepository;
import org.polytech.covidapi.dto.AppointmentView;
import org.polytech.covidapi.dto.AppointmentsCenterView;
import org.polytech.covidapi.dto.DayView;
import org.polytech.covidapi.entities.Appointment;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.bytebuddy.asm.Advice.Local;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CenterRepository centerRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/public/center/{id}/appointments")
    AppointmentsCenterView findAllAppointmentsAvailableByCenterId(@PathVariable("id") int center_id) { // TO DO : like

        List<DayView> days = new ArrayList<DayView>();
        List<AppointmentView> appointmentsAvailable = new ArrayList<AppointmentView>();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().minusSeconds(LocalTime.now().getSecond())
                .minusNanos(LocalTime.now().getNano());
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime closeTime = LocalTime.of(18, 0);
        Center center = centerRepository.findFirstById(center_id);

        for (int day = 0; day < 7; day++) {
            LocalDate newDate = date.plusDays(day);
            List<Appointment> appointmentsUnavailable = appointmentRepository.findAppointmentsByCenterAndDate(center,
                    newDate);
            List<LocalTime> timeUnavailable = new ArrayList<LocalTime>();
            for (Appointment a : appointmentsUnavailable) {
                timeUnavailable.add(a.getTime());
            }

            if (day == 0) {
                int unroundedMinutes = time.getMinute();
                int mod = unroundedMinutes % 15;
                LocalTime timeRounded = time.plusMinutes((15 - mod));
                while (timeRounded.isBefore(closeTime)) {
                    if (!timeUnavailable.contains(timeRounded)) {
                        AppointmentView appointment = new AppointmentView(timeRounded, center.getId());
                        appointmentsAvailable.add(appointment);
                    }
                    timeRounded = timeRounded.plusMinutes(15);
                }
            } else {
                LocalTime baseTime = LocalTime.of(8, 0); // 8h
                while (baseTime.isBefore(closeTime)) // Jusqu'a 18h
                {
                    if (!timeUnavailable.contains(baseTime)) {
                        AppointmentView appointment = new AppointmentView(baseTime, center.getId());
                        appointmentsAvailable.add(appointment);
                    }
                    baseTime = baseTime.plusMinutes(15);
                }
            }
            DayView dayView = new DayView(newDate, appointmentsAvailable);
            days.add(dayView);
        }
        AppointmentsCenterView appointments = new AppointmentsCenterView(days, startTime, closeTime);
        return appointments;
    }

    @PostMapping(path = "/public/center/{id}/appointments")
    void registerAppointment(@PathVariable("id") int center_id, @RequestParam("doctor_id") int doctor_id,
            @RequestBody User user, @RequestParam("date") String date, @RequestParam("time") String time) {
        Center center = centerRepository.findFirstById(center_id);
        User doctor = userRepository.findFirstById(doctor_id);
        LocalDate dateParsed = LocalDate.parse(date);
        LocalTime timeParsed = LocalTime.parse(time);
        userRepository.save(user);
        Optional<User> userSaved = userRepository.findFirstByEmail(user.getEmail());
        Appointment appointmentScheduled = new Appointment(dateParsed, timeParsed, false, center, userSaved.get(), doctor);
        appointmentRepository.save(appointmentScheduled);
    }

}
