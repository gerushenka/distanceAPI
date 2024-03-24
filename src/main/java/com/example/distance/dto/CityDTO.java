package com.example.distance.dto;

import lombok.Getter;
import lombok.Setter;

public class CityDTO {
    @Getter @Setter private Long id;
    @Getter @Setter private String name;
    @Getter @Setter private double latitude;
    @Getter @Setter private double longitude;
}