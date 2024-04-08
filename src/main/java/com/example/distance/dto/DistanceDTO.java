package com.example.distance.dto;

import lombok.Getter;
import lombok.Setter;

public class DistanceDto {

  @Getter
  @Setter
  private Long id;
  @Getter
  @Setter
  private String cityFirst;
  @Getter
  @Setter
  private String citySecond;
  @Getter
  @Setter
  private double cityDistance;

}