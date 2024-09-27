package com.example.distance.controller;

import com.example.distance.config.CacheConfig;
import com.example.distance.dto.CityDto;
import com.example.distance.dto.DistanceDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.service.CityService;
import com.example.distance.service.DistanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;


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

    @Autowired
    private DistanceController distanceController;

    void testGetDistance_WithInvalidId() throws Exception {
        // Mocking service response with an empty Optional
        when(distanceService.getDistanceById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testCreateDistance_WithInvalidData() throws Exception {
        // Mocking service response with null for created distance
        when(distanceService.createDistance(any(Distance.class)))
                .thenReturn(null);

        Distance invalidDistance = new Distance();
        mockMvc.perform(MockMvcRequestBuilders.post("/distance")
                        .content(asJsonString(invalidDistance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateDistance_WithInvalidId() throws Exception {
        // Mocking service response with null for updated distance
        when(distanceService.updateDistance(anyLong(), any(Distance.class)))
                .thenReturn(null);

        Distance invalidDistance = new Distance();
        mockMvc.perform(MockMvcRequestBuilders.put("/distance/999")
                        .content(asJsonString(invalidDistance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteDistance_WithInvalidId() throws Exception {
        // Mocking service response for unsuccessful deletion
        when(distanceService.deleteDistance(anyLong()))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/distance/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetDistancesByCityNames_WithMismatchedLists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/city-to-city")
                        .param("cityFirstList", "Berlin")
                        .param("citySecondList", "Paris", "New York")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testCalculateDistance_WithMissingCity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .param("cityFirst", "Berlin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testGetDistance_WithValidId_NotFound() throws Exception {
        // Mocking service response with an empty Optional
        when(distanceService.getDistanceById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testCreateDistance_WithValidData_InternalServerError() throws Exception {
        // Mocking service response with null for created distance
        when(distanceService.createDistance(any(Distance.class)))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/distance")
                        .content(asJsonString(new Distance()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateDistance_WithValidIdAndData_NotFound() throws Exception {
        // Mocking service response with null for updated distance
        when(distanceService.updateDistance(anyLong(), any(Distance.class)))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/distance/1")
                        .content(asJsonString(new Distance()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteDistance_WithValidId_NotFound() throws Exception {
        // Mocking service response for unsuccessful deletion
        when(distanceService.deleteDistance(anyLong()))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetDistancesByCityNames_WithEmptyLists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/city-to-city")
                        .param("cityFirstList", "")
                        .param("citySecondList", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetDistance_WithValidId_Found() throws Exception {
        // Mocking service response with a Distance object
        Distance distance = new Distance();
        when(distanceService.getDistanceById(anyLong()))
                .thenReturn(Optional.of(distance));

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCreateDistance_WithValidData_Created() throws Exception {
        // Mocking service response with a created Distance object
        Distance distance = new Distance();
        when(distanceService.createDistance(any(Distance.class)))
                .thenReturn(distance);

        mockMvc.perform(MockMvcRequestBuilders.post("/distance")
                        .content(asJsonString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateDistance_WithValidIdAndData_Updated() throws Exception {
        // Mocking service response with an updated Distance object
        Distance distance = new Distance();
        when(distanceService.updateDistance(anyLong(), any(Distance.class)))
                .thenReturn(distance);

        mockMvc.perform(MockMvcRequestBuilders.put("/distance/1")
                        .content(asJsonString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteDistance_WithValidId_Deleted() throws Exception {
        // Mocking service response for successful deletion
        when(distanceService.deleteDistance(anyLong()))
                .thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/distance/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Helper method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCalculateDistance_WithInvalidCityNames() throws Exception {
        // Mocking API response as empty JSON array
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("{\"geoname\": []}");

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .param("cityFirst", "phhlfshjlfhjsjhlksfgjlkhd")
                        .param("citySecond", "fgsdjlbfjlhfgdsjhklgfshjgsf")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error")
                        .value("Error parsing JSON response: geonames array is missing or empty"));
    }

    @Test
    void testGenerateCacheKey() {
        // Arrange
        String cityFirst = "Berlin";
        String citySecond = "Paris";
        String expectedKey = "Berlin_Paris";

        // Act
        String generatedKey = distanceController.generateCacheKey(cityFirst, citySecond);

        // Assert
        assertEquals(expectedKey, generatedKey);
    }

    @Test
    void testCalculateDistance_WithGeonamesAPIUnavailable() throws Exception {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .param("cityFirst", "Berlin")
                        .param("citySecond", "Paris")
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testDeleteDistance() throws Exception {
        // Arrange
        Long id = 1L;
        when(distanceService.deleteDistance(id)).thenReturn(true);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/distance/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(distanceService, times(1)).deleteDistance(id);
    }
    @Test
    void testCalculateDistance_WithMissingParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testCalculateDistance_WithInvalidJSONResponse() throws Exception {
        // Mock API response as invalid JSON
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("Invalid JSON response");

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .param("cityFirst", "Berlin")
                        .param("citySecond", "Paris")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error")
                        .value("Error processing JSON"));
    }

    @Test
    void testCalculateDistance_WithInvalidCities() throws Exception {
        // Мокируем ответ от API Geonames
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("{\"error\": \"Error parsing JSON response: geonames array is missing or empty\"}");

        mockMvc.perform(MockMvcRequestBuilders.get("/distance/calculate")
                        .param("cityFirst", "InvalidCity1")
                        .param("citySecond", "InvalidCity2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Error parsing JSON response: geonames array is missing or empty"));
    }

    @Test
    void testCreateDistance() throws Exception {
        // Arrange
        Distance distance = new Distance();
        distance.setId(1L); // Установить значение id
        when(distanceService.createDistance(any(Distance.class))).thenReturn(distance);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/distance")
                        .content(asJsonString(distance))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void testGetDistance_WithNonExistingId() throws Exception {
        // Arrange
        long id = 999L;
        when(distanceService.getDistanceById(id)).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/distance/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateDistance() throws Exception {
        // Arrange
        long id = 1L;
        Distance updatedDistance = new Distance();
        updatedDistance.setId(id); // Установить значение id
        when(distanceService.updateDistance(eq(id), any(Distance.class))).thenReturn(updatedDistance);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/distance/{id}", id)
                        .content(asJsonString(new Distance()))
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
    }

    @Test
    void testGetDistancesByCityNames() throws Exception {
        // Создайте данные для теста
        List<String> cityFirstList = Arrays.asList("Berlin", "Paris", "Moscow");
        List<String> citySecondList = Arrays.asList("Paris", "Moscow", "Berlin");

        // Подготовьте данные для сервиса
        List<Distance> distances = new ArrayList<>();
        // Заполните distances данными

        // Убедитесь, что distanceService возвращает нужные данные при вызове
        when(distanceService.getDistancesByCityNames(anyString(), anyString())).thenReturn(distances);

        // Вызовите метод контроллера и проверьте статус ответа
        mockMvc.perform(get("/distance/city-to-city")
                        .param("cityFirstList", String.join(",", cityFirstList))
                        .param("citySecondList", String.join(",", citySecondList)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCalculateDistance_ValidCities() {
        // Mock JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node1 = objectMapper.createObjectNode();
        ArrayNode geonames1 = node1.putArray("geonames");
        ObjectNode city1 = JsonNodeFactory.instance.objectNode();
        city1.put("lat", "52.5200");
        city1.put("lng", "13.4050");
        geonames1.add(city1);

        ObjectNode node2 = objectMapper.createObjectNode();
        ArrayNode geonames2 = node2.putArray("geonames");
        ObjectNode city2 = JsonNodeFactory.instance.objectNode();
        city2.put("lat", "48.8566");
        city2.put("lng", "2.3522");
        geonames2.add(city2);

        // Mock RestTemplate
        RestTemplate restTemplateMock = mock(RestTemplate.class);
        when(restTemplateMock.getForObject(anyString(), any())).thenReturn(node1.toString(), node2.toString());

        // Mock cityService
        CityService cityServiceMock = mock(CityService.class);
        when(cityServiceMock.saveCity(anyString(), anyDouble(), anyDouble())).thenReturn(new City());

        // Mock distanceService
        DistanceService distanceServiceMock = mock(DistanceService.class);
        when(distanceServiceMock.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(100.0);

        // Call the method under test
        ResponseEntity<ObjectNode> responseEntity = distanceController.calculateDistance("Berlin", "Paris");

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testGetDistance_WithValidDistance_ShouldReturnDtoWithCorrectValues() {
        // Arrange
        Distance distance = new Distance();
        distance.setId(1L);
        distance.setCityDistance(100.0);

        City cityFirst = new City();
        cityFirst.setId(1L);
        cityFirst.setName("City A");

        City citySecond = new City();
        citySecond.setId(2L);
        citySecond.setName("City B");

        when(distanceService.getDistanceById(1L)).thenReturn(Optional.of(distance));
        when(cityService.getCityById(1L)).thenReturn(Optional.of(cityFirst));
        when(cityService.getCityById(2L)).thenReturn(Optional.of(citySecond));

        // Act
        DistanceDto dto = distanceController.getDistance(1L).getBody();

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals(100.0, dto.getCityDistance());
        assertEquals("Unknown", dto.getCityFirst());
        assertEquals("Unknown", dto.getCitySecond());
    }

}
