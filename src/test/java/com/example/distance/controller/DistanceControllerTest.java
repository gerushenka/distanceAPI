package com.example.distance.controller;

import com.example.distance.config.CacheConfig;
import com.example.distance.dto.DistanceDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DistanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DistanceService distanceService;

    @Mock
    private CityService cityService;

    @Mock
    private CacheConfig cacheConfig;

    @InjectMocks
    private DistanceController distanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(distanceController).build();
    }



    @Test
    void testUpdateDistance() throws Exception {
        Distance distance = new Distance();
        distance.setId(1L);
        when(distanceService.updateDistance(eq(1L), any())).thenReturn(distance);

        mockMvc.perform(put("/distance/1")
                        .content(new ObjectMapper().writeValueAsString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteDistance() throws Exception {
        when(distanceService.deleteDistance(1L)).thenReturn(true);

        mockMvc.perform(delete("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
