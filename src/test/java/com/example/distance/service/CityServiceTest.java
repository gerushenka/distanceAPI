package com.example.distance.service;

import com.example.distance.dto.CityDto;
import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.repository.CityRepository;
import com.example.distance.repository.DistanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DistanceRepository distanceRepository;

    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cityService = new CityService(cityRepository, distanceRepository);
    }

    @Test
    void testSaveCity_NewCity() {
        // Given
        String name = "TestCity";
        double latitude = 10.0;
        double longitude = 20.0;
        City existingCity = new City();
        existingCity.setName(name);
        existingCity.setLatitude(latitude);
        existingCity.setLongitude(longitude);
        when(cityRepository.findByName(name)).thenReturn(existingCity);

        // When
        City savedCity = cityService.saveCity(name, latitude, longitude);

        // Then
        assertNotNull(savedCity);
        assertEquals(existingCity, savedCity); // Verify that the existing city is returned
        verify(cityRepository, times(1)).findByName(name);
        verify(cityRepository, never()).save(any(City.class)); // Ensure that save method is never called
    }

    @Test
    void testSaveCity_ExistingCity() {
        // Given
        String name = "TestCity";
        double latitude = 10.0;
        double longitude = 20.0;
        City existingCity = new City();
        existingCity.setName(name);
        existingCity.setLatitude(latitude);
        existingCity.setLongitude(longitude);
        when(cityRepository.findByName(name)).thenReturn(existingCity);

        // When
        City savedCity = cityService.saveCity(name, latitude, longitude);

        // Then
        assertNotNull(savedCity);
        assertEquals(existingCity, savedCity);
        verify(cityRepository, times(1)).findByName(name);
        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void testCreateCity() {
        // Given
        CityDto cityDto = new CityDto();
        cityDto.setName("TestCity");
        cityDto.setLatitude(10.0);
        cityDto.setLongitude(20.0);

        // Mock existing city with the same name
        City existingCity = new City();
        existingCity.setName(cityDto.getName());
        existingCity.setLatitude(30.0);
        existingCity.setLongitude(40.0);
        when(cityRepository.findByName(cityDto.getName())).thenReturn(existingCity);

        // When
        City createdCity = cityService.createCity(cityDto);

        // Then
        assertNotNull(createdCity);
        assertEquals(existingCity, createdCity); // Verify that the existing city is returned
        verify(cityRepository, never()).save(any(City.class)); // Ensure that save method is never called
    }


    @Test
    void testGetCityById() {
        // Given
        Long id = 1L;
        City city = new City();
        city.setId(id);
        when(cityRepository.findById(id)).thenReturn(Optional.of(city));

        // When
        Optional<City> retrievedCity = cityService.getCityById(id);

        // Then
        assertTrue(retrievedCity.isPresent());
        assertEquals(city, retrievedCity.get());
        verify(cityRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateCity() {
        // Given
        Long id = 1L;
        City existingCity = new City();
        existingCity.setId(id);
        existingCity.setName("ExistingCity");
        existingCity.setLatitude(20.0);
        existingCity.setLongitude(30.0);
        City updatedCity = new City();
        updatedCity.setId(id);
        updatedCity.setName("UpdatedCity");
        updatedCity.setLatitude(30.0);
        updatedCity.setLongitude(40.0);
        when(cityRepository.findById(id)).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(any(City.class))).thenReturn(updatedCity); // Мокируем возвращаемое значение для метода save()

        // When
        City result = cityService.updateCity(id, updatedCity);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedCity.getName(), result.getName());
        assertEquals(updatedCity.getLatitude(), result.getLatitude());
        assertEquals(updatedCity.getLongitude(), result.getLongitude());
        verify(cityRepository, times(1)).findById(id);
        verify(cityRepository, times(1)).save(existingCity);
    }


    @Test
    void testDeleteCityAndRelatedDistances() {
        // Given
        Long id = 1L;
        City city = new City();
        city.setId(id);
        List<Distance> distances = new ArrayList<>();
        distances.add(new Distance());
        when(cityRepository.findById(id)).thenReturn(Optional.of(city));
        when(distanceRepository.findByCityFirstOrCitySecond(city, city)).thenReturn(distances);

        // When
        boolean deleted = cityService.deleteCityAndRelatedDistances(id);

        // Then
        assertTrue(deleted);
        verify(cityRepository, times(1)).findById(id);
        verify(distanceRepository, times(1)).findByCityFirstOrCitySecond(city, city);
        verify(distanceRepository, times(1)).deleteAll(distances);
        verify(cityRepository, times(1)).delete(city);
    }
}
