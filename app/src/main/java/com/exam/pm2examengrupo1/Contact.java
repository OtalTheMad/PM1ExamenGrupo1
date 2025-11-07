package com.exam.pm2examengrupo1;

import java.io.Serializable;

public class Contact implements Serializable {


    private int id;
    private String name;
    private String phone;
    private double latitude;
    private double longitude;
    private String imagePath;


    public Contact(int id, String name, String phone, double latitude, double longitude, String imagePath) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getImagePath() { return imagePath; }
}
