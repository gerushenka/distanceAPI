package com.example.distance.model.distancedto;

public class DistanceDTO {
    private Long id;
    private String cityFirst;
    private String citySecond;
    private double cityDistance;

    public Long getId() {
        return id;
    }

    public double getCityDistance() {
        return cityDistance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCityDistance(double distance) {
        this.cityDistance = distance;
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