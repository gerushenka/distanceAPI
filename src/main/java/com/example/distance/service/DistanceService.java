package com.example.distance.service;

import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.repository.CityRepository;
import com.example.distance.repository.DistanceRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public class DistanceService {
  private final DistanceRepository distanceRepository;
  private final CityRepository cityRepository;

  public DistanceService(DistanceRepository distanceRepository, CityRepository cityRepository) {
    this.distanceRepository = distanceRepository;
    this.cityRepository = cityRepository;
  }

  public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    double earthRadius = 6371.0;

    double lat1Radians = Math.toRadians(lat1);
    double lon1Radians = Math.toRadians(lon1);
    double lat2Radians = Math.toRadians(lat2);
    double lon2Radians = Math.toRadians(lon2);

    double deltaLat = lat2Radians - lat1Radians;
    double deltaLon = lon2Radians - lon1Radians;

    double a = Math.pow(Math.sin(deltaLat / 2), 2)
        + Math.cos(lat1Radians) * Math.cos(lat2Radians) * Math.pow(Math.sin(deltaLon / 2), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return earthRadius * c;
  }

  public Distance saveDistance(double distance, City cityFirst, City citySecond) {
    Optional<Distance> existingDistance = distanceRepository.findByCityDistance(distance);

    if (existingDistance.isPresent()) {
      return existingDistance.get();
    } else {
      Distance newDistance = new Distance();

      newDistance.setCityDistance(distance);
      newDistance.setCityFirst(cityFirst);
      newDistance.setCitySecond(citySecond);
      return distanceRepository.save(newDistance);
    }
  }

  public Optional<Distance> getDistanceById(Long id) {
    return distanceRepository.findById(id);
  }

  public Distance createDistance(Distance distance) {
    return distanceRepository.save(distance);
  }

  public Distance updateDistance(Long id, Distance updatedDistance) {
    if (distanceRepository.existsById(id)) {
      updatedDistance.setId(id);
      return distanceRepository.save(updatedDistance);
    } else {
      return null;
    }
  }

  public boolean deleteDistance(Long id) {
    if (distanceRepository.existsById(id)) {
      distanceRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }

  public List<Distance> getDistancesByCityNames(String city1Name, String city2Name) {
    City city1 = cityRepository.findByName(city1Name);
    City city2 = cityRepository.findByName(city2Name);

    return distanceRepository.findByCityNames(city1.getName(), city2.getName());
  }
}