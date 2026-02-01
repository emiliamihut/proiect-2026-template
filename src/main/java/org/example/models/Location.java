package org.example.models;

import org.example.exceptions.LocationException;

// locatia unui server
public class Location {
    private String country;
    private String city;
    private String address;

    public Location(String country, String city, String address) {
        // tara e obligatorie
        if (country == null || country.isEmpty()) {
            throw new LocationException();
        }
        this.country = country;
        this.city = city;
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }
}

