package com.example.distance.service;

import com.example.distance.dto.CityDto;
import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
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

  public City createCity(CityDto cityDto) {
    City existingCity = cityRepository.findByName(cityDto.getName());

    if (existingCity != null) {
      return existingCity; // Возвращаем существующий город, если он найден
    } else {
      City city = new City();
      city.setName(cityDto.getName());
      city.setLatitude(cityDto.getLatitude());
      city.setLongitude(cityDto.getLongitude());
      return cityRepository.save(city);
    }
  }

  public Optional<City> getCityById(Long id) {
    return cityRepository.findById(id);
  }

  public City updateCity(Long id, City updatedCity) {
    Optional<City> existingCityOptional = cityRepository.findById(id);
    if (existingCityOptional.isPresent()) {
      City existingCity = existingCityOptional.get();
      existingCity.setName(updatedCity.getName());
      existingCity.setLatitude(updatedCity.getLatitude());
      existingCity.setLongitude(updatedCity.getLongitude());
      return cityRepository.save(existingCity);
    } else {
      return null;
    }
  }

  public boolean deleteCityAndRelatedDistances(Long id) {
    Optional<City> cityEntityOptional = cityRepository.findById(id);
    if (cityEntityOptional.isPresent()) {
      City city = cityEntityOptional.get();
      List<Distance> relatedDistances = distanceRepository.findByCityFirstOrCitySecond(city, city);
      distanceRepository.deleteAll(relatedDistances);
      cityRepository.delete(city);
      return true;
    }
    return false;
  }
}
