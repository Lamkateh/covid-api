package org.polytech.covidapi.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(columnDefinition = "boolean default false")
    private boolean disabled;

    @ElementCollection
    private List<String> roles;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Appointment> appointmentsAsPatient;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Appointment> appointmentsAsDoctor;

    @ManyToOne(targetEntity = Center.class)
    private Center center;

    // Getters
    public Integer getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPhone() {
        return this.phone;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public List<Appointment> getAppointmentsAsPatient() {
        return this.appointmentsAsPatient;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public List<Appointment> getAppointmentsAsDoctor() {
        return this.appointmentsAsDoctor;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public Center getCenter() {
        return this.center;
    }

    public boolean getDisabled() {
        return this.disabled;
    }

    // Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setRole(String role) {
        List<String> roles = new ArrayList<>();
        roles.add(role);
        this.roles = roles;
    }

    public void setAppointmentsAsPatient(List<Appointment> appointmentsAsPatient) {
        this.appointmentsAsPatient = appointmentsAsPatient;
    }

    public void setAppointmentsAsDoctor(List<Appointment> appointmentsAsDoctor) {
        this.appointmentsAsDoctor = appointmentsAsDoctor;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
