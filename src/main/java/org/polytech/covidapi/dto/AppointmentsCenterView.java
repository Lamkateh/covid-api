package org.polytech.covidapi.dto;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AppointmentsCenterView {
    private List<DayView> days;
    private LocalTime startTime;
    private LocalTime closeTime;

    public AppointmentsCenterView() {

    }

    public AppointmentsCenterView(List<DayView> days, LocalTime startTime, LocalTime closeTime) {
        this.days = days;
        this.startTime = startTime;
        this.closeTime = closeTime;
    }

    public List<DayView> getDays() {
        return days;
    }

    public void setDays(List<DayView> days) {
        this.days = days;
    }

    @JsonFormat(pattern = "HH:mm")
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "HH:mm")
    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }
}
