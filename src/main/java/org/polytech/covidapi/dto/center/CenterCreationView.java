package org.polytech.covidapi.dto.center;

import org.polytech.covidapi.entities.Center;

public class CenterCreationView {

    private int id;
    private String name;
    private String address;
    private String city;
    private String zipCode;
    private String phone;
    private String email;

    public CenterCreationView(Center center) {
        this.id = center.getId();
        this.name = center.getName();
        this.address = center.getAddress();
        this.city = center.getCity();
        this.zipCode = center.getZipCode();
        this.phone = center.getPhone();
        this.email = center.getEmail();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
