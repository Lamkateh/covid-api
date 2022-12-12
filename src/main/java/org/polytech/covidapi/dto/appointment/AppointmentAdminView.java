package org.polytech.covidapi.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.polytech.covidapi.dto.ProfileView;
import org.polytech.covidapi.entities.Appointment;

public class AppointmentAdminView {

    private int id;
    private LocalTime time;
    private LocalDate date;
    private ProfileView patient;
    private ProfileView doctor;
    private boolean isDone;

    public AppointmentAdminView() { }

    public AppointmentAdminView(Appointment appointment) {
        this.id = appointment.getId();
        this.time = appointment.getTime();
        this.date = appointment.getDate();
        this.isDone = appointment.getIsDone();
        if (appointment.getPatient() != null) {
            this.patient = new ProfileView(appointment.getPatient());
        } else {
            this.patient = null;
        }
        if (appointment.getDoctor() != null) {
            this.doctor = new ProfileView(appointment.getDoctor());
        } else {
            this.doctor = null;
        }
    }

    public int getId() {
        return id;
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

    public ProfileView getDoctor() { return doctor; }

    public boolean getIsDone() {
        return isDone;
    }

    public static List<AppointmentAdminView> convert(List<Appointment> center) {
        return center.stream().map(AppointmentAdminView::new).toList();
    }

}
