package com.example.distance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Distance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  @Setter
  private Long id;

  @Getter
  @Setter
  private double cityDistance;

  @ManyToOne(fetch = FetchType.LAZY)
  @Getter
  @Setter
  private City cityFirst;

  @ManyToOne(fetch = FetchType.LAZY)
  @Getter
  @Setter
  private City citySecond;


}

