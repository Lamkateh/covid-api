package org.polytech.covidapi.dto;

import java.time.LocalDate;
import java.util.List;

public class DayView {

    private LocalDate Date;
    private List<AppointmentView> appointments;

    public DayView() {

    }

    public DayView(LocalDate date, List<AppointmentView> appointments) {
        Date = date;
        this.appointments = appointments;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public List<AppointmentView> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentView> appointments) {
        this.appointments = appointments;
    }
}
