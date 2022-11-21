package org.polytech.covidapi.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AppointmentView {

    private LocalTime time;
    private LocalDate date;
    private int centerId;

    public AppointmentView() {

    }

    public AppointmentView(LocalTime time, LocalDate date, int centerId) {
        this.time = time;
        this.date = date;
        this.centerId = centerId;
    }

    @JsonFormat(pattern = "HH:mm")
    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }
}
