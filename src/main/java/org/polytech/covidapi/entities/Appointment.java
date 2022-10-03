package org.polytech.covidapi.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    @Column(name = "is_done")
    private boolean isDone;
    @ManyToOne(fetch = FetchType.LAZY)
    private Center center;

    @ManyToOne(fetch = FetchType.LAZY)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    private User doctor;


    public Appointment(){

    }
    public Appointment(LocalDate date, LocalTime time) {
        this.date =date; 
        this.time = time;
        //this.center = center;
    }

    public Appointment(LocalDate date, LocalTime time, boolean isDone, Center center, User patient, User doctor) {
        this.date = date;
        this.time = time;
        this.isDone = isDone;
        this.center = center;
        this.patient = patient;
        this.doctor = doctor;
    }

    // Getters
    public Integer getId() {
        return this.id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public Center getCenter() {
        return this.center;
    }

    public User getPatient() {
        return this.patient;
    }

    public User getDoctor() {
        return this.doctor;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
}
