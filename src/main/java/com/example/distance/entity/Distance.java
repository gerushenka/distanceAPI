package com.example.distance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Distance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;

    @Getter @Setter private double cityDistance;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter @Setter private City cityFirst;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter @Setter private City citySecond;



}

