package com.example.distance.service;

import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.repository.CityRepository;
import com.example.distance.repository.DistanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistanceServiceTest {

    @Mock
    private DistanceRepository distanceRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private DistanceService distanceService;

    private City city1;
    private City city2;

    @BeforeEach
    void setUp() {
        city1 = new City();
        city1.setName("Москва");
        city1.setLatitude(55.7558);
        city1.setLongitude(37.6173);

        city2 = new City();
        city2.setName("Санкт-Петербург");
        city2.setLatitude(59.9343);
        city2.setLongitude(30.3351);
    }

    @Test
    void calculateDistanceTest() {
        double distance = distanceService.calculateDistance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
        assertTrue(distance > 0);
    }



    @Test
    void saveDistanceExistingTest() {
        Distance distance = new Distance();
        distance.setCityDistance(100);
        distance.setCityFirst(city1);
        distance.setCitySecond(city2);

        when(distanceRepository.findByCityDistance(anyDouble())).thenReturn(Optional.of(distance));

        Distance savedDistance = distanceService.saveDistance(100, city1, city2);

        assertEquals(distance, savedDistance);
        verify(distanceRepository, never()).save(any(Distance.class));
    }

    @Test
    void getDistanceByIdTest() {
        Distance distance = new Distance();
        distance.setId(1L);

        when(distanceRepository.findById(1L)).thenReturn(Optional.of(distance));

        Optional<Distance> foundDistance = distanceService.getDistanceById(1L);

        assertTrue(foundDistance.isPresent());
        assertEquals(distance, foundDistance.get());
    }

    @Test
    void getDistanceByIdNotFoundTest() {
        when(distanceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Distance> foundDistance = distanceService.getDistanceById(1L);

        assertTrue(foundDistance.isEmpty());
    }

    @Test
    void createDistanceTest() {
        Distance distance = new Distance();

        when(distanceRepository.save(any(Distance.class))).thenReturn(distance);

        Distance createdDistance = distanceService.createDistance(distance);

        assertEquals(distance, createdDistance);
        verify(distanceRepository, times(1)).save(distance);
    }

    @Test
    void updateDistanceTest() {
        Distance existingDistance = new Distance();
        existingDistance.setId(1L);

        Distance updatedDistance = new Distance();
        updatedDistance.setCityDistance(200);

        when(distanceRepository.existsById(1L)).thenReturn(true);
        when(distanceRepository.save(any(Distance.class))).thenReturn(updatedDistance);

        Distance result = distanceService.updateDistance(1L, updatedDistance);

        assertEquals(updatedDistance, result);
        verify(distanceRepository, times(1)).save(updatedDistance);
    }

    @Test
    void updateDistanceNotFoundTest() {
        Distance updatedDistance = new Distance();

        when(distanceRepository.existsById(1L)).thenReturn(false);

        Distance result = distanceService.updateDistance(1L, updatedDistance);

        assertNull(result);
        verify(distanceRepository, never()).save(any(Distance.class));
    }

    @Test
    void deleteDistanceTest() {
        when(distanceRepository.existsById(1L)).thenReturn(true);

        boolean result = distanceService.deleteDistance(1L);

        assertTrue(result);
        verify(distanceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDistanceNotFoundTest() {
        when(distanceRepository.existsById(1L)).thenReturn(false);

        boolean result = distanceService.deleteDistance(1L);

        assertFalse(result);
        verify(distanceRepository, never()).deleteById(anyLong());
    }

    @Test
    void getDistancesByCityNamesTest() {
        Distance distance = new Distance();
        distance.setCityDistance(100);
        distance.setCityFirst(city1);
        distance.setCitySecond(city2);

        when(cityRepository.findByName("Москва")).thenReturn(city1);
        when(cityRepository.findByName("Санкт-Петербург")).thenReturn(city2);
        when(distanceRepository.findByCityNames("Москва", "Санкт-Петербург")).thenReturn(List.of(distance));

        List<Distance> distances = distanceService.getDistancesByCityNames("Москва", "Санкт-Петербург");

        assertEquals(1, distances.size());
        assertEquals(distance, distances.get(0));
    }


    @Test
    void testSaveDistance_ExistingDistance() {
        // Arrange
        double distanceValue = 100.0;
        City cityFirst = new City();
        cityFirst.setId(1L);
        City citySecond = new City();
        citySecond.setId(2L);

        Distance existingDistance = new Distance();
        existingDistance.setId(1L);
        existingDistance.setCityDistance(distanceValue);
        existingDistance.setCityFirst(cityFirst);
        existingDistance.setCitySecond(citySecond);

        when(distanceRepository.findByCityDistance(distanceValue)).thenReturn(Optional.of(existingDistance));

        // Act
        Distance savedDistance = distanceService.saveDistance(distanceValue, cityFirst, citySecond);

        // Assert
        assertEquals(existingDistance, savedDistance);
        verify(distanceRepository, times(1)).findByCityDistance(distanceValue);
        verify(distanceRepository, never()).save(any());
    }

    @Test
    void testSaveDistance_NewDistance() {
        // Arrange
        double distanceValue = 100.0;
        City cityFirst = new City();
        cityFirst.setId(1L);
        City citySecond = new City();
        citySecond.setId(2L);

        when(distanceRepository.findByCityDistance(distanceValue)).thenReturn(Optional.empty());

        // Act
        Distance savedDistance = distanceService.saveDistance(distanceValue, cityFirst, citySecond);

        // Assert
        assertNull(savedDistance.getId()); // New distance should not have an ID yet
        assertEquals(distanceValue, savedDistance.getCityDistance());
        assertEquals(cityFirst, savedDistance.getCityFirst());
        assertEquals(citySecond, savedDistance.getCitySecond());
        verify(distanceRepository, times(1)).findByCityDistance(distanceValue);
        verify(distanceRepository, times(1)).save(any());
    }
}