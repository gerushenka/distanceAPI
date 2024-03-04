package com.example.distance.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double latitude;
    private double longitude;


    @OneToMany(mappedBy = "city1")
    private List<DistanceEntity> distancesFrom;

    @OneToMany(mappedBy = "city2")
    private List<DistanceEntity> distancesTo;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
