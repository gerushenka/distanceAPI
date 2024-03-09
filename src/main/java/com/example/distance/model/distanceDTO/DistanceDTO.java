package com.example.distance.model.distancedto;


import java.util.List;

public class DistanceDTO {
    private Long id;
    private String city1;
    private String city2;
    private List<String> cities;
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

    public String getCity1() {
        return city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }


}