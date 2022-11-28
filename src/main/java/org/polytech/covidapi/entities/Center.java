package org.polytech.covidapi.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "center", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "center", fetch = FetchType.LAZY)
    private List<User> staff;

    public Center() {

    }

    public Center(int id, String name, String address, String city, String zipCode, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.phone = phone;
        this.email = email;
    }

    // Getters with trim
    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name != null ? this.name.trim() : this.name;
    }

    public String getAddress() {
        return this.address != null ? this.address.trim() : this.address;
    }

    public String getCity() {
        return this.city != null ? this.city.trim() : this.city;
    }

    public String getZipCode() {
        return this.zipCode != null ? this.zipCode.trim() : this.zipCode;
    }

    public String getPhone() {
        return this.phone != null ? this.phone.trim() : this.phone;
    }

    public String getEmail() {
        return this.email != null ? this.email.trim() : this.email;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public List<Appointment> getAppointments() {
        return this.appointments;
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public List<User> getDoctors() {
        return this.staff.stream().filter(user -> user.getRoles().contains("DOCTOR")).toList();
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public List<User> getAdmins() {
        return this.staff.stream().filter(user -> user.getRoles().contains("ADMIN")).toList();
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addDoctor(User doctor) {
        doctor.setRole("DOCTOR");
        this.staff.add(doctor);
    }

    public void removeDoctor(User doctor) {
        doctor.setRole("USER");
        this.staff.remove(doctor);
    }

    public void addAdmin(User admin) {
        admin.setRole("ADMIN");
        this.staff.add(admin);
    }

    public void removeAdmin(User admin) {
        admin.setRole("USER");
        this.staff.remove(admin);
    }
}
