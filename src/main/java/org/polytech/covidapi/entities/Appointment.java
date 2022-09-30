package org.polytech.covidapi.entities;

import java.util.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    private Date date;
    private Time time;
    @Column(name = "is_done")
    private boolean isDone;
    @ManyToOne
    private Center center;

    @ManyToOne
    private User patient;

    @ManyToOne
    private User doctor;

    // Getters
    public Integer getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public Time getTime() {
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
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
