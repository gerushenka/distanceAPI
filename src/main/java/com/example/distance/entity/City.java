package com.example.distance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  @Setter
  private Long id;
  @Getter
  @Setter
  private String name;
  @Getter
  @Setter
  private double latitude;
  @Getter
  @Setter
  private double longitude;

}
