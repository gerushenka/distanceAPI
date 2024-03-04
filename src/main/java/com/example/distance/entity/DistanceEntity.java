package com.example.distance.entity;

import jakarta.persistence.*;

@Entity
public class DistanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;

    @ManyToOne
    @JoinColumn(name = "city1")
    private CityEntity city1;

    @ManyToOne
    @JoinColumn(name = "city2")
    private CityEntity city2;


    public Long getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public CityEntity getCity1() {
        return city1;
    }

    public CityEntity getCity2() {
        return city2;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setCity1(CityEntity city1) {
        this.city1 = city1;
    }

    public void setCity2(CityEntity city2) {
        this.city2 = city2;
    }
}

