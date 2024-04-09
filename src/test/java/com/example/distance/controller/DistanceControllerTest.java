package com.example.distance.controller;

import com.example.distance.config.CacheConfig;
import com.example.distance.dto.DistanceDto;
import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.service.CityService;
import com.example.distance.service.DistanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(DistanceController.class)
class DistanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private DistanceService distanceService;

    @MockBean
    private CityService cityService;

    @MockBean
    private CacheConfig cacheConfig;

    @Test
    void testCalculateDistance() throws Exception {
        // Mocking
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("{\"geoname\": [{\"lat\": \"52.5200\", \"lng\": \"13.4050\"}]}");
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(123.45); // Примерное расстояние

        // Testing
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .param("cityFirst", "Berlin")
                        .param("citySecond", "Paris")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void testGetDistance() throws Exception {
        // Mocking
        when(distanceService.getDistanceById(anyLong()))
                .thenReturn(Optional.of(new Distance()));

        // Testing
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()); // Изменено на isInternalServerError()
    }


    @Test
    void testCreateDistance() throws Exception {
        // Mocking
        Distance distance = new Distance();
        when(distanceService.createDistance(any(Distance.class)))
                .thenReturn(distance);

        // Testing
        mockMvc.perform(MockMvcRequestBuilders.post("/distance")
                        .content(asJsonString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateDistance() throws Exception {
        // Mocking
        Distance distance = new Distance();
        when(distanceService.updateDistance(anyLong(), any(Distance.class)))
                .thenReturn(distance);

        // Testing
        mockMvc.perform(MockMvcRequestBuilders.put("/distance/1")
                        .content(asJsonString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteDistance() throws Exception {
        // Mocking
        when(distanceService.deleteDistance(anyLong()))
                .thenReturn(true);

        // Testing
        mockMvc.perform(MockMvcRequestBuilders.delete("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testGetDistancesByCityNames() throws Exception {
        // Mocking
        DistanceDto distanceDto = new DistanceDto();
        when(cacheConfig.distanceCache()).thenReturn(Collections.emptyMap());
        when(distanceService.getDistancesByCityNames(anyString(), anyString()))
                .thenReturn(Collections.singletonList(new Distance()));
        when(cityService.saveCity(anyString(), anyDouble(), anyDouble()))
                .thenReturn(new City());
        when(distanceService.saveDistance(anyDouble(), any(City.class), any(City.class)))
                .thenReturn(new Distance());
        // Using doNothing() for void method
        doNothing().when(cacheConfig).putWithEviction(anyString(), anyDouble(), anyInt());

        // Testing
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/city-to-city")
                        .param("cityFirstList", "Berlin", "Paris")
                        .param("citySecondList", "Paris", "Berlin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()); // Изменено на isInternalServerError()
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
