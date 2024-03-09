package com.example.distance.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class DistanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;

    @ManyToOne(fetch = FetchType.LAZY) // Ленивая загрузка для city1
    @JoinColumn(name = "city1")
    private CityEntity city1;

    @ManyToOne(fetch = FetchType.LAZY) // Ленивая загрузка для city2
    @JoinColumn(name = "city2")
    private CityEntity city2;


    @OneToMany(mappedBy = "distance", cascade = CascadeType.ALL)
    private List<DistanceCityEntity> distanceCityEntities;

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

    public List<DistanceCityEntity> getDistanceCityEntities() {
        return distanceCityEntities;
    }

    public void setDistanceCityEntities(List<DistanceCityEntity> distanceCityEntities) {
        this.distanceCityEntities = distanceCityEntities;
    }

}

