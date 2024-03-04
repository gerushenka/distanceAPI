package com.example.distance.service;

import com.example.distance.entity.CityEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DistanceService {
    private final CityService cityService;

    public DistanceService(CityService cityService) {

        this.cityService = cityService;
    }

        // Метод для вычисления расстояния между двумя координатами по формуле гаверсинусов
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Радиус Земли в километрах
        double earthRadius = 6371.0;

        // Переводим координаты в радианы
        double lat1Radians = Math.toRadians(lat1);
        double lon1Radians = Math.toRadians(lon1);
        double lat2Radians = Math.toRadians(lat2);
        double lon2Radians = Math.toRadians(lon2);

        // Вычисляем разницу между координатами
        double deltaLat = lat2Radians - lat1Radians;
        double deltaLon = lon2Radians - lon1Radians;

        // Вычисляем расстояние по формуле гаверсинусов
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }
    public void saveCity(String cityName, double latitude, double longitude) {
        cityService.saveCity(cityName, latitude, longitude);
    }
}