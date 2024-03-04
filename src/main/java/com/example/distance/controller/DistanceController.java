package com.example.distance.controller;

import com.example.distance.repository.CityRepo;
import com.example.distance.service.CityService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.example.distance.service.DistanceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;

@RestController
@RequestMapping("/distance")
public class DistanceController {


    @Autowired
    private CityRepo cityRepo;

    @Value("${geoname.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final DistanceService distanceService;

    public DistanceController(RestTemplate restTemplate, DistanceService distanceService, CityService cityService) {
        this.restTemplate = restTemplate;
        this.distanceService = distanceService;
    }

    @GetMapping("/calculate")
    public ResponseEntity calculateDistance(@RequestParam String city1, @RequestParam String city2) {

        // Получаем координаты каждого города с помощью GeoNames API
        String url1 = "http://api.geonames.org/searchJSON?q=" + city1 + "&maxRows=1&username=" + apiKey;
        String url2 = "http://api.geonames.org/searchJSON?q=" + city2 + "&maxRows=1&username=" + apiKey;

        // Отправляем GET-запросы к API GeoNames и получаем ответы в виде JSON
        String jsonResponse1 = restTemplate.getForObject(url1, String.class);
        String jsonResponse2 = restTemplate.getForObject(url2, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode node1 = objectMapper.readTree(jsonResponse1);
            JsonNode node2 = objectMapper.readTree(jsonResponse2);


            // Извлекаем координаты широты и долготы для каждого города
            double lat1 = Double.parseDouble(node1.path("geonames").get(0).path("lat").asText());
            double lng1 = Double.parseDouble(node1.path("geonames").get(0).path("lng").asText());
            double lat2 = Double.parseDouble(node2.path("geonames").get(0).path("lat").asText());
            double lng2 = Double.parseDouble(node2.path("geonames").get(0).path("lng").asText());

            distanceService.saveCity(city1, lat1, lng1);
            distanceService.saveCity(city2, lat2, lng2);

            // Вычисляем расстояние между двумя координатами
            double distance = distanceService.calculateDistance(lat1, lng1, lat2, lng2);
            DecimalFormat df = new DecimalFormat("#.##");

            // Применение формата к переменной distance
            String formattedDistance = df.format(distance);
            ObjectNode responseJson = objectMapper.createObjectNode();
            responseJson.put("city1", city1);
            responseJson.put("city2", city2);
            responseJson.put("distance_km", formattedDistance);
            return ResponseEntity.ok(responseJson);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке JSON");
        }
    }

}
