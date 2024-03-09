package com.example.distance.entity;

import jakarta.persistence.*;

@Entity
public class DistanceCityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private DistanceEntity distance;

    @ManyToOne(fetch = FetchType.LAZY)
    private CityEntity city;

    public Long getId() {
        return id;
    }

    public DistanceEntity getDistance() {
        return distance;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDistance(DistanceEntity distance) {
        this.distance = distance;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }
}