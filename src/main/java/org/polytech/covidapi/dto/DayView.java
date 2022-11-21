package org.polytech.covidapi.dto;

import java.time.LocalDate;
import java.util.List;

import org.polytech.covidapi.dto.appointment.AppointmentPreviewView;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DayView {

    private LocalDate Date;
    private List<AppointmentPreviewView> appointments;

    public DayView() {

    }

    public DayView(LocalDate date, List<AppointmentPreviewView> appointments) {
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

    public List<AppointmentPreviewView> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentPreviewView> appointments) {
        this.appointments = appointments;
    }
}
