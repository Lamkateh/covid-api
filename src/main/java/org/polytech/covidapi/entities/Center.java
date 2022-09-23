package org.polytech.covidapi.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "centers")
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    private String city;

    @Column(name = "zip_code")
    private String zipCode;
    private String phone;
    private String email;

    @OneToMany(mappedBy = "center")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "center")
    private List<User> doctors;
}
