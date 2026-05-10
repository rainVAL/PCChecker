package com.pcchecker.model;

import java.io.Serializable;

public class Shop implements Serializable {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String description;

    public Shop(String name, String address, double latitude, double longitude, String description) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getDescription() { return description; }
}
