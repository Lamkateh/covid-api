package org.polytech.covidapi.dto.center;

import java.util.List;

import org.polytech.covidapi.entities.Center;

public class CenterPreviewView {
    private int id;
    private String name;
    public String address;
    public String city;
    public String zipCode;
    public String phone;
    public String email;

    public CenterPreviewView(Center center) {
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

    public static List<CenterPreviewView> convert(List<Center> center) {
        return center.stream().map(CenterPreviewView::new).toList();
    }
}
