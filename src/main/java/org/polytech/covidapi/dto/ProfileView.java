package org.polytech.covidapi.dto;

import java.util.List;

import org.polytech.covidapi.entities.User;

public class ProfileView {
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;

    public ProfileView() {
    }

    public ProfileView(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
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
}
