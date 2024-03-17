package com.example.distance.entity;

import jakarta.persistence.*;

@Entity
public class Distance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "distance")
    private double cityDistance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city1")
    private City cityFirst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city2")
    private City citySecond;



    public Long getId() {
        return id;
    }

    public City getCity1() {
        return cityFirst;
    }

    public City getCity2() {
        return citySecond;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCity1(City cityFirst) {
        this.cityFirst = cityFirst;
    }

    public void setCity2(City citySecond) {
        this.citySecond = citySecond;
    }

    public double getCityDistance() {
        return cityDistance;
    }

    public void setCityDistance(double cityDistance) {
        this.cityDistance = cityDistance;
    }
}

