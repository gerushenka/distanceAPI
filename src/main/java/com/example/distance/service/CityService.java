package com.example.distance.service;

import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.model.citydto.CityDataTransferObject;
import com.example.distance.repository.CityRepository;
import com.example.distance.repository.DistanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;
    private final DistanceRepository distanceRepository;
    public CityService(CityRepository cityRepository, DistanceRepository distanceRepository) {

        this.cityRepository = cityRepository;
        this.distanceRepository = distanceRepository;
    }

    public City saveCity(String name, double latitude, double longitude) {
        City existingCity = cityRepository.findByName(name);

        if (existingCity != null) {
            return existingCity;
        } else {
            City city = new City();
            city.setName(name);
            city.setLatitude(latitude);
            city.setLongitude(longitude);
            return cityRepository.save(city);
        }
    }

    public City createCity(CityDataTransferObject cityDataTransferObject) {
        City city = new City();
        city.setName(cityDataTransferObject.getName());
        city.setLatitude(cityDataTransferObject.getLatitude());
        city.setLongitude(cityDataTransferObject.getLongitude());
        return cityRepository.save(city);
    }

    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    public City updateCity(Long id, City updatedCity) {
        if (cityRepository.existsById(id)) {
            updatedCity.setId(id);
            return cityRepository.save(updatedCity);
        } else {
            return null;
        }
    }

    public boolean deleteCityAndRelatedDistances(Long id) {
        Optional<City> cityEntityOptional = cityRepository.findById(id);
        if (cityEntityOptional.isPresent()) {
            City city = cityEntityOptional.get();
            List<Distance> relatedDistances = distanceRepository.findByCity1OrCity2(city, city);
            distanceRepository.deleteAll(relatedDistances);
            cityRepository.delete(city);



            return true;
        }
        return false;
    }

}
