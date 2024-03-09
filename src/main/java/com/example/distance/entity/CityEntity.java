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


    @OneToMany(mappedBy = "city1", fetch = FetchType.LAZY) // Ленивая загрузка для distancesFrom
    private List<DistanceEntity> distancesFrom;

    @OneToMany(mappedBy = "city2", fetch = FetchType.LAZY) // Ленивая загрузка для distancesTo
    private List<DistanceEntity> distancesTo;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<DistanceCityEntity> distanceCityEntities;

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

    public List<DistanceEntity> getDistancesFrom() {
        return distancesFrom;
    }

    public List<DistanceEntity> getDistancesTo() {
        return distancesTo;
    }

    public List<DistanceCityEntity> getDistanceCityEntities() {
        return distanceCityEntities;
    }

    public void setDistancesFrom(List<DistanceEntity> distancesFrom) {
        this.distancesFrom = distancesFrom;
    }

    public void setDistancesTo(List<DistanceEntity> distancesTo) {
        this.distancesTo = distancesTo;
    }

    public void setDistanceCityEntities(List<DistanceCityEntity> distanceCityEntities) {
        this.distanceCityEntities = distanceCityEntities;
    }
}
