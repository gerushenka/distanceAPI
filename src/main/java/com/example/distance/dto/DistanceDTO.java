package com.example.distance.dto;

import lombok.Getter;
import lombok.Setter;

public class DistanceDTO {

    @Getter @Setter private Long id;
    @Getter @Setter private String cityFirst;
    @Getter @Setter private String citySecond;
    @Getter @Setter private double cityDistance;

}