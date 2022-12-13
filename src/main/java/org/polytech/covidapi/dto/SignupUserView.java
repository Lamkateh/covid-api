package org.polytech.covidapi.dto;

import java.time.LocalDate;
import java.util.List;

import org.polytech.covidapi.entities.User;

public class SignupUserView {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> roles;
    private Integer centerId;
    private String birthDate;
    private String phone;
    private Boolean disabled;
    public SignupUserView() {
    }

    public SignupUserView(User user) { 
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        if (user.getCenter() != null) {
            this.centerId = user.getCenter().getId();
        } else {
            this.centerId = null;
        }
        this.birthDate = user.getBirthDate().toString();
        this.phone = user.getPhone();
        this.disabled = user.getDisabled();
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

    public Integer getCenterId() {
        return centerId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getDisabled() { return disabled; }

    public String getPassword() {
        return password;
    }

}