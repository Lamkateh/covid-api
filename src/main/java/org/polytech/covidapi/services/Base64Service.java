package org.polytech.covidapi.services;

import java.util.Base64;

public class Base64Service {
    public static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}
