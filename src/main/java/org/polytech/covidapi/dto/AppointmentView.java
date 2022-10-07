package org.polytech.covidapi.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentView {

    private LocalTime Time;
    private int centerId;

    public LocalTime getTime() {
        return Time;
    }

    public AppointmentView(){

    }
    public AppointmentView(LocalTime time, int centerId) {
        Time = time;
        this.centerId = centerId;
    }

    public void setTime(LocalTime time) {
        Time = time;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }
}
