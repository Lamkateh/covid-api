package org.polytech.covidapi.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentView {

    private LocalTime Time;
    private LocalDate Date;
    private int centerId;

    public LocalTime getTime() {
        return Time;
    }

    public AppointmentView(){

    }
    public AppointmentView(LocalTime time, LocalDate date, int centerId) {
        Time = time;
        Date = date;
        this.centerId = centerId;
    }

    public void setTime(LocalTime time) {
        Time = time;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }
}
