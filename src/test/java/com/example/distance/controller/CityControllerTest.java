package com.example.distance.controller;

import com.example.distance.dto.CityDto;
import com.example.distance.entity.City;
import com.example.distance.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateCity() {
        CityDto cityDto = new CityDto();
        cityDto.setId(1L);
        cityDto.setName("Test City");
        cityDto.setLatitude(40.7128);
        cityDto.setLongitude(-74.0060);

        City createdCity = new City();
        createdCity.setId(1L);
        createdCity.setName("Test City");
        createdCity.setLatitude(40.7128);
        createdCity.setLongitude(-74.0060);

        when(cityService.createCity(cityDto)).thenReturn(createdCity);

        ResponseEntity<CityDto> response = cityController.createCity(cityDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdCity.getId(), response.getBody().getId());
        assertEquals(createdCity.getName(), response.getBody().getName());
        assertEquals(createdCity.getLatitude(), response.getBody().getLatitude());
        assertEquals(createdCity.getLongitude(), response.getBody().getLongitude());

        verify(cityService, times(1)).createCity(cityDto);
    }

    @Test
    void testGetCity() {
        Long id = 1L;
        City city = new City();
        city.setId(id);
        city.setName("Test City");
        city.setLatitude(40.7128);
        city.setLongitude(-74.0060);

        when(cityService.getCityById(id)).thenReturn(Optional.of(city));

        ResponseEntity<CityDto> response = cityController.getCity(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(city.getId(), response.getBody().getId());
        assertEquals(city.getName(), response.getBody().getName());
        assertEquals(city.getLatitude(), response.getBody().getLatitude());
        assertEquals(city.getLongitude(), response.getBody().getLongitude());

        verify(cityService, times(1)).getCityById(id);
    }

    // Similarly, you can write tests for updateCity() and deleteCity() methods
}
