package com.example.distance.service;

import com.example.distance.entity.CityEntity;
import com.example.distance.repository.CityRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService {
    private final CityRepo cityRepo;

    public CityService(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }

    public CityEntity saveCity(String name, double latitude, double longitude) {
        CityEntity existingCity = cityRepo.findByName(name);

        if (existingCity != null) {
            return existingCity;
        } else {
            CityEntity city = new CityEntity();
            city.setName(name);
            city.setLatitude(latitude);
            city.setLongitude(longitude);
            return cityRepo.save(city);
        }
    }

    public CityEntity createCity(CityEntity city) {
        return cityRepo.save(city);
    }

    public Optional<CityEntity> getCityById(Long id) {
        return cityRepo.findById(id);
    }

    public CityEntity updateCity(Long id, CityEntity updatedCity) {
        if (cityRepo.existsById(id)) {
            updatedCity.setId(id);
            return cityRepo.save(updatedCity);
        } else {
            return null;
        }
    }

    public boolean deleteCity(Long id) {
        if (cityRepo.existsById(id)) {
            cityRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


}
