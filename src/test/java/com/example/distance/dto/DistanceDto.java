package com.example.distance.dto;

import com.example.distance.dto.DistanceDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DistanceDtoTest {

    @Test
    void testGettersAndSetters() {
        DistanceDto distanceDto = new DistanceDto();

        // Устанавливаем значения
        distanceDto.setId(1L);
        distanceDto.setCityFirst("City1");
        distanceDto.setCitySecond("City2");
        distanceDto.setCityDistance(100.0);

        // Проверяем, что получаем ожидаемые значения
        assertEquals(1L, distanceDto.getId());
        assertEquals("City1", distanceDto.getCityFirst());
        assertEquals("City2", distanceDto.getCitySecond());
        assertEquals(100.0, distanceDto.getCityDistance());
    }

    @Test
    void testEqualsAndHashCode() {
        DistanceDto distanceDto1 = new DistanceDto();
        distanceDto1.setId(1L);
        distanceDto1.setCityFirst("City1");
        distanceDto1.setCitySecond("City2");
        distanceDto1.setCityDistance(100.0);

        DistanceDto distanceDto2 = new DistanceDto();
        distanceDto2.setId(1L);
        distanceDto2.setCityFirst("City1");
        distanceDto2.setCitySecond("City2");
        distanceDto2.setCityDistance(100.0);

        assertEquals(distanceDto1, distanceDto2);
        assertEquals(distanceDto1.hashCode(), distanceDto2.hashCode());

        distanceDto2.setId(2L);
        assertNotEquals(distanceDto1, distanceDto2);
        assertNotEquals(distanceDto1.hashCode(), distanceDto2.hashCode());
    }

    @Test
    void testToString() {
        DistanceDto distanceDto = new DistanceDto();
        distanceDto.setId(1L);
        distanceDto.setCityFirst("City1");
        distanceDto.setCitySecond("City2");
        distanceDto.setCityDistance(100.0);

        String expectedToString = "DistanceDto{id=1, cityFirst='City1', citySecond='City2', cityDistance=100.0}";
        assertEquals(expectedToString, distanceDto.toString());
    }
}
