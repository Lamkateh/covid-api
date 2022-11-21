package org.polytech.covidapi.dto;

import java.time.LocalDate;
import java.util.List;

import org.polytech.covidapi.entities.User;

public class ProfileView {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private Integer center_id;
    private LocalDate birthDate;
    private String phone;

    public ProfileView() {
    }

    public ProfileView(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        if (user.getCenter() != null) {
            this.center_id = user.getCenter().getId();
        } else {
            this.center_id = null;
        }
        this.birthDate = user.getBirthDate();
        this.phone = user.getPhone();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Integer getCenter_id() {
        return center_id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }
}
