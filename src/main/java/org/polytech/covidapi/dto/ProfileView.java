package org.polytech.covidapi.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.polytech.covidapi.dto.center.CenterPreviewView;
import org.polytech.covidapi.entities.Center;
import org.polytech.covidapi.entities.User;

public class ProfileView {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private CenterPreviewView center;
    private LocalDate birthDate;
    private String phone;
    private boolean disabled;
    public ProfileView() {
    }

    public ProfileView(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        if (user.getCenter() != null) {
            this.center = new CenterPreviewView(user.getCenter());
        } else {
            this.center = null;
        }
        this.birthDate = user.getBirthDate();
        this.phone = user.getPhone();
        this.disabled = user.getDisabled();
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

    public CenterPreviewView getCenter() {
        return center;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public static List<ProfileView> convert(List<User> user) {
        return user.stream().map(ProfileView::new).toList();
    }
}