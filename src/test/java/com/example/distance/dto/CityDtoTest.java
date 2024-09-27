package com.example.distance.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityDtoTest {

    @Test
    void getterSetterTest() {
        CityDto cityDto = new CityDto();

        Long id = 1L;
        String name = "Москва";
        double latitude = 55.7558;
        double longitude = 37.6173;

        cityDto.setId(id);
        cityDto.setName(name);
        cityDto.setLatitude(latitude);
        cityDto.setLongitude(longitude);

        assertEquals(id, cityDto.getId());
        assertEquals(name, cityDto.getName());
        assertEquals(latitude, cityDto.getLatitude());
        assertEquals(longitude, cityDto.getLongitude());
    }

}