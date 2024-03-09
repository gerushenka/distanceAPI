package com.example.distance.service;

import com.example.distance.entity.CityEntity;
import com.example.distance.entity.DistanceCityEntity;
import com.example.distance.entity.DistanceEntity;
import com.example.distance.repository.DistanceCityRepo;
import com.example.distance.repository.DistanceRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DistanceService {
    private final DistanceRepo distanceRepo;
    private final DistanceCityRepo distanceCityRepo;
    public DistanceService(DistanceRepo distanceRepo, DistanceCityRepo distanceCityRepo) {
        this.distanceCityRepo = distanceCityRepo;
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

        return earthRadius * c;
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
            DistanceEntity savedDistance = distanceRepo.save(newDistance);

            // Создаем связь с таблицей distance_city_entity для city1
            DistanceCityEntity distanceCity1 = new DistanceCityEntity();
            distanceCity1.setDistance(savedDistance);
            distanceCity1.setCity(city1);
            distanceCityRepo.save(distanceCity1);

            // Создаем связь с таблицей distance_city_entity для city2
            DistanceCityEntity distanceCity2 = new DistanceCityEntity();
            distanceCity2.setDistance(savedDistance);
            distanceCity2.setCity(city2);
            distanceCityRepo.save(distanceCity2);

            return savedDistance;
        }
    }

    public Optional<DistanceEntity> getDistanceById(Long id) {
        return distanceRepo.findById(id);
    }

    public DistanceEntity createDistance(DistanceEntity distanceEntity) {
        return distanceRepo.save(distanceEntity);
    }

    public DistanceEntity updateDistance(Long id, DistanceEntity updatedDistance) {
        if (distanceRepo.existsById(id)) {
            updatedDistance.setId(id);
            return distanceRepo.save(updatedDistance);
        } else {
            return null;
        }
    }

    public boolean deleteDistance(Long id) {
        if (distanceRepo.existsById(id)) {
            distanceRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}