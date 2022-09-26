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
}
