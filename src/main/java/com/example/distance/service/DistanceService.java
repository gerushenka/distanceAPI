package com.example.distance.service;

import com.example.distance.entity.CityEntity;
import com.example.distance.entity.DistanceEntity;
import com.example.distance.repository.DistanceRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DistanceService {
    private final DistanceRepo distanceRepo;

    public DistanceService(DistanceRepo distanceRepo) {
        this.distanceRepo = distanceRepo;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371.0;

        double lat1Radians = Math.toRadians(lat1);
        double lon1Radians = Math.toRadians(lon1);
        double lat2Radians = Math.toRadians(lat2);
        double lon2Radians = Math.toRadians(lon2);

        double deltaLat = lat2Radians - lat1Radians;
        double deltaLon = lon2Radians - lon1Radians;

        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }

    public DistanceEntity saveDistance(double distance, CityEntity city1, CityEntity city2) {
        Optional<DistanceEntity> existingDistance = distanceRepo.findByDistance(distance);

        if (existingDistance.isPresent()) {
            return existingDistance.get();
        } else {
            DistanceEntity newDistance = new DistanceEntity();
            newDistance.setDistance(distance);
            newDistance.setCity1(city1);
            newDistance.setCity2(city2);
            return distanceRepo.save(newDistance);
        }
    }
}