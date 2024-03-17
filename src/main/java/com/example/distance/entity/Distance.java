package com.example.distance.entity;

import jakarta.persistence.*;

@Entity
public class Distance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city1")
    private City city1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city2")
    private City city2;



    public Long getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setCity1(City city1) {
        this.city1 = city1;
    }

    public void setCity2(City city2) {
        this.city2 = city2;
    }


}

