package org.polytech.covidapi.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AppointmentView {

    private LocalTime Time;
    private int centerId;

    public AppointmentView() {

    }

    public AppointmentView(LocalTime time, int centerId) {
        Time = time;
        this.centerId = centerId;
    }

    @JsonFormat(pattern = "HH:mm")
    public LocalTime getTime() {
        return Time;
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
