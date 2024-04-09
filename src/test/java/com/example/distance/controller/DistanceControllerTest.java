package com.example.distance.controller;

import com.example.distance.config.CacheConfig;
import com.example.distance.controller.DistanceController;
import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.service.CityService;
import com.example.distance.service.DistanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(DistanceController.class)
class DistanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DistanceService distanceService;

    @MockBean
    private CityService cityService;

    @MockBean
    private CacheConfig cacheConfig;

    @InjectMocks
    private DistanceController distanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCalculateDistance() throws Exception {
        when(cityService.saveCity(anyString(), anyDouble(), anyDouble())).thenReturn(new City());
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(100.0);

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .param("cityFirst", "City1")
                        .param("citySecond", "City2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance_km").value("100.00"));
    }

    @Test
    void testGetDistance() throws Exception {
        Distance distance = new Distance();
        distance.setId(1L);
        when(distanceService.getDistanceById(1L)).thenReturn(Optional.of(distance));

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void testCreateDistance() throws Exception {
        Distance distance = new Distance();
        when(distanceService.createDistance(any())).thenReturn(distance);

        mockMvc.perform(MockMvcRequestBuilders.post("/distance")
                        .content(new ObjectMapper().writeValueAsString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void testUpdateDistance() throws Exception {
        Distance distance = new Distance();
        distance.setId(1L);
        when(distanceService.updateDistance(eq(1L), any())).thenReturn(distance);

        mockMvc.perform(MockMvcRequestBuilders.put("/distance/1")
                        .content(new ObjectMapper().writeValueAsString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteDistance() throws Exception {
        when(distanceService.deleteDistance(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetDistancesByCityNames() throws Exception {
        Distance distance = new Distance();
        List<Distance> distances = Collections.singletonList(distance);
        when(distanceService.getDistancesByCityNames(anyString(), anyString())).thenReturn(distances);

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/city-to-city")
                        .param("cityFirstList", "City1")
                        .param("citySecondList", "City2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }
}
