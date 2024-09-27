package com.example.distance.service;

import com.example.distance.dto.CityDto;
import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.repository.CityRepository;
import com.example.distance.repository.DistanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DistanceRepository distanceRepository;

    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cityService = new CityService(cityRepository, distanceRepository);
    }

    @Test
    void saveCity_ExistingCity_ReturnsExistingCity() {
        String cityName = "ExistingCity";
        City existingCity = new City();
        existingCity.setId(1L);
        existingCity.setName(cityName);
        when(cityRepository.findByName(cityName)).thenReturn(existingCity);

        City savedCity = cityService.saveCity(cityName, 0.0, 0.0);

        assertEquals(existingCity, savedCity);
    }

    @Test
    void saveCity_NewCity_ReturnsSavedCity() {
        String cityName = "NewCity";
        when(cityRepository.findByName(cityName)).thenReturn(null);
        // Создаем объект City для возврата при сохранении
        City cityToSave = new City();
        cityToSave.setName(cityName);
        when(cityRepository.save(any())).thenReturn(cityToSave);

        City savedCity = cityService.saveCity(cityName, 0.0, 0.0);

        assertNotNull(savedCity);
        assertEquals(cityName, savedCity.getName());
    }


    @Test
    void createCity_ExistingCity_ReturnsExistingCity() {
        String cityName = "ExistingCity";
        CityDto cityDto = new CityDto();
        cityDto.setName(cityName);
        City existingCity = new City();
        existingCity.setId(1L);
        existingCity.setName(cityName);
        when(cityRepository.findByName(cityName)).thenReturn(existingCity);

        City createdCity = cityService.createCity(cityDto);

        assertEquals(existingCity, createdCity);
    }

    @Test
    void createCity_NewCity_ReturnsSavedCity() {
        String cityName = "NewCity";
        CityDto cityDto = new CityDto();
        cityDto.setName(cityName);
        when(cityRepository.findByName(cityName)).thenReturn(null);
        // Создаем объект City для возврата при сохранении
        City savedCity = new City();
        savedCity.setName(cityName);
        when(cityRepository.save(any())).thenReturn(savedCity);

        City createdCity = cityService.createCity(cityDto);

        assertNotNull(createdCity);
        assertEquals(cityName, createdCity.getName());
    }

    @Test
    void getCityById_CityExists_ReturnsCity() {
        Long cityId = 1L;
        City city = new City();
        city.setId(cityId);
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        Optional<City> result = cityService.getCityById(cityId);

        assertTrue(result.isPresent());
        assertEquals(cityId, result.get().getId());
    }

    @Test
    void getCityById_CityNotExists_ReturnsEmptyOptional() {
        Long cityId = 1L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        Optional<City> result = cityService.getCityById(cityId);

        assertFalse(result.isPresent());
    }

    @Test
    void updateCity_CityExists_ReturnsUpdatedCity() {
        Long cityId = 1L;
        City updatedCity = new City();
        updatedCity.setName("UpdatedCity");
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(new City()));
        when(cityRepository.save(any())).thenReturn(updatedCity);

        City result = cityService.updateCity(cityId, updatedCity);

        assertNotNull(result);
        assertEquals(updatedCity.getName(), result.getName());
    }

    @Test
    void updateCity_CityNotExists_ReturnsNull() {
        Long cityId = 1L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        City result = cityService.updateCity(cityId, new City());

        assertNull(result);
    }

    @Test
    void deleteCityAndRelatedDistances_CityExists_ReturnsTrue() {
        Long cityId = 1L;
        City city = new City();
        city.setId(cityId);
        List<Distance> relatedDistances = new ArrayList<>();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(distanceRepository.findByCityFirstOrCitySecond(city, city)).thenReturn(relatedDistances);

        boolean result = cityService.deleteCityAndRelatedDistances(cityId);

        assertTrue(result);
        verify(distanceRepository).deleteAll(relatedDistances);
        verify(cityRepository).delete(city);
    }

    @Test
    void deleteCityAndRelatedDistances_CityNotExists_ReturnsFalse() {
        Long cityId = 1L;
        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        boolean result = cityService.deleteCityAndRelatedDistances(cityId);

        assertFalse(result);
        verify(distanceRepository, never()).deleteAll(any());
        verify(cityRepository, never()).delete(any());
    }
}
