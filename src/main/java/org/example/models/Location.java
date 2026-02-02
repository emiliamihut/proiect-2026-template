package org.example.models;

import org.example.exceptions.LocationException;

// locatia unui server
public class Location {
    private final String country;
    private final String city;
    private final String address;
    private final Double latitude;
    private final Double longitude;

    // builder pentru creare usoara
    public static class LocationBuilder {
        private String country;
        private String city;
        private String address;
        private Double latitude;
        private Double longitude;

        public LocationBuilder country(String country) {
            this.country = country;
            return this;
        }

        public LocationBuilder city(String city) {
            this.city = city;
            return this;
        }

        public LocationBuilder address(String address) {
            this.address = address;
            return this;
        }

        public LocationBuilder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public LocationBuilder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Location build() {
            return new Location(country, city, address, latitude, longitude);
        }
    }

    private Location(String country, String city, String address, Double latitude, Double longitude) {
        // tara e obligatorie
        if (country == null || country.isEmpty()) {
            throw new LocationException();
        }
        this.country = country;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}

