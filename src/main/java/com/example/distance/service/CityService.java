package com.example.distance.service;

import com.example.distance.entity.CityEntity;
import com.example.distance.repository.CityRepo;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private final CityRepo cityRepo;

    public CityService(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public CityEntity saveCity(String name, double latitude, double longitude) {
        CityEntity existingCity = cityRepo.findByName(name);

        if (existingCity != null) {
            // Если город уже существует, возвращаем его без сохранения
            return existingCity;
        } else {
            // Если город не существует, создаем новый объект города и сохраняем его
            CityEntity city = new CityEntity();
            city.setName(name);
            city.setLatitude(latitude);
            city.setLongitude(longitude);
            return cityRepo.save(city);
        }
    }

}
