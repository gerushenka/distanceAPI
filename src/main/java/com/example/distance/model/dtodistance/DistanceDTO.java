package com.example.distance.model.dtodistance;

public class DistanceDTO {
    private Long id;
    private String cityFirst;
    private String citySecond;
    private double distance;

    public Long getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCityFirst() {
        return cityFirst;
    }

    public String getCitySecond() {
        return citySecond;
    }

    public void setCityFirst(String cityFirst) {
        this.cityFirst = cityFirst;
    }

    public void setCitySecond(String citySecond) {
        this.citySecond = citySecond;
    }
}