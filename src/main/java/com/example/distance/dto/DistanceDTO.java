package com.example.distance.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DistanceDto that = (DistanceDto) o;
    return Double.compare(that.cityDistance, cityDistance) == 0 &&
            Objects.equals(id, that.id) &&
            Objects.equals(cityFirst, that.cityFirst) &&
            Objects.equals(citySecond, that.citySecond);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cityFirst, citySecond, cityDistance);
  }

  @Override
  public String toString() {
    return "DistanceDto{" +
            "id=" + id +
            ", cityFirst='" + cityFirst + '\'' +
            ", citySecond='" + citySecond + '\'' +
            ", cityDistance=" + cityDistance +
            '}';
  }
}