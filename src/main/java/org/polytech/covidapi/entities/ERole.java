package org.polytech.covidapi.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ERole {
    USER,
    DOCTOR,
    ADMIN,
    SUPERADMIN;

    private String text;

    @Override
    public String toString() {
        return text;
    }

    public String getText(){
        return this.text;
    }

    @JsonCreator
    public static ERole fromText(String text) {
        for(ERole r : ERole.values()){
            if(r.getText().equals(text)){
                return r;
            }
        }
        throw new IllegalArgumentException();
    }
}
