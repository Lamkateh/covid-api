package org.polytech.covidapi.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DayView {

    private LocalDate Date;
    private List<AppointmentView> appointments;

    public DayView() {

    }

    public DayView(LocalDate date, List<AppointmentView> appointments) {
        Date = date;
        this.appointments = appointments;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
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
