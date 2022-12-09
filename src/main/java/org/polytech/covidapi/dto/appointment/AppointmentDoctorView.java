package org.polytech.covidapi.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.polytech.covidapi.dto.ProfileView;
import org.polytech.covidapi.entities.Appointment;

public class AppointmentDoctorView {

    private LocalTime time;
    private LocalDate date;
    private ProfileView patient;

    public AppointmentDoctorView() {

    }

    public AppointmentDoctorView(Appointment appointment) {
        this.time = appointment.getTime();
        this.date = appointment.getDate();
        if (appointment.getPatient() != null) {
            this.patient = new ProfileView(appointment.getPatient());
        } else {
            this.patient = null;
        }
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public ProfileView getPatient() {
        return patient;
    }

    public static List<AppointmentDoctorView> convert(List<Appointment> center) {
        return center.stream().map(AppointmentDoctorView::new).toList();
    }

}
