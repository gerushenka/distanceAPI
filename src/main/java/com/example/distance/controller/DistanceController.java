package com.example.distance.controller;

import com.example.distance.entity.CityEntity;
import com.example.distance.service.CityService;
import com.fasterxml.jackson.databind.node.ObjectNode;
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




    @Value("${geoname.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final DistanceService distanceService;
    private final CityService cityService;

    public DistanceController(RestTemplate restTemplate, DistanceService distanceService, CityService cityService) {
        this.restTemplate = restTemplate;
        this.distanceService = distanceService;
        this.cityService = cityService;
    }

    @GetMapping("/calculate")
    public ResponseEntity calculateDistance(@RequestParam String city1Name, @RequestParam String city2Name) {

        String url1 = "http://api.geonames.org/searchJSON?q=" + city1Name + "&maxRows=1&username=" + apiKey;
        String url2 = "http://api.geonames.org/searchJSON?q=" + city2Name + "&maxRows=1&username=" + apiKey;
        String jsonResponse1 = restTemplate.getForObject(url1, String.class);
        String jsonResponse2 = restTemplate.getForObject(url2, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode node1 = objectMapper.readTree(jsonResponse1);
            JsonNode node2 = objectMapper.readTree(jsonResponse2);


            String geonames = "geonames";
            double lat1 = Double.parseDouble(node1.path(geonames).get(0).path("lat").asText());
            double lng1 = Double.parseDouble(node1.path(geonames).get(0).path("lng").asText());
            double lat2 = Double.parseDouble(node2.path(geonames).get(0).path("lat").asText());
            double lng2 = Double.parseDouble(node2.path(geonames).get(0).path("lng").asText());

            CityEntity city1 = cityService.saveCity(city1Name, lat1, lng1);
            CityEntity city2 = cityService.saveCity(city2Name, lat2, lng2);

            double distance = distanceService.calculateDistance(lat1, lng1, lat2, lng2);
            DecimalFormat df = new DecimalFormat("#.##");
            distanceService.saveDistance(distance, city1, city2);
            String formattedDistance = df.format(distance);
            ObjectNode responseJson = objectMapper.createObjectNode();
            responseJson.put("city1", city1Name);
            responseJson.put("city2", city2Name);
            responseJson.put("distance_km", formattedDistance);
            return ResponseEntity.ok(responseJson);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке JSON");
        }
    }

}
