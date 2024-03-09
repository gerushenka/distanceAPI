package com.example.distance.controller;

import com.example.distance.entity.CityEntity;
import com.example.distance.entity.DistanceEntity;
import com.example.distance.model.distanceDTO.DistanceDTO;
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
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<DistanceDTO> getDistance(@PathVariable Long id) {
        Optional<DistanceEntity> distanceEntityOptional = distanceService.getDistanceById(id);
        if (distanceEntityOptional.isPresent()) {
            DistanceEntity distanceEntity = distanceEntityOptional.get();
            DistanceDTO distanceDTO = convertToDTO(distanceEntity);
            return ResponseEntity.ok(distanceDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    private DistanceDTO convertToDTO(DistanceEntity distanceEntity) {
        DistanceDTO dto = new DistanceDTO();
        dto.setId(distanceEntity.getId());
        dto.setDistance(distanceEntity.getDistance());

        // Получаем объекты CityEntity по их идентификаторам
        Optional<CityEntity> city1Optional = cityService.getCityById(distanceEntity.getCity1().getId());
        Optional<CityEntity> city2Optional = cityService.getCityById(distanceEntity.getCity2().getId());

        // Проверяем, существуют ли города
        if (city1Optional.isPresent() && city2Optional.isPresent()) {
            // Получаем названия городов из объектов CityEntity
            String city1Name = city1Optional.get().getName();
            String city2Name = city2Optional.get().getName();

            // Устанавливаем названия городов в DTO
            dto.setCity1(city1Name);
            dto.setCity2(city2Name);
        } else {
            // Если города не найдены, можно сделать что-то другое, например, установить значения по умолчанию или бросить исключение
            // В данном случае, я просто устанавливаю "Unknown" вместо названий городов
            dto.setCity1("Unknown");
            dto.setCity2("Unknown");
        }

        return dto;
    }

    @PostMapping
    public ResponseEntity<DistanceEntity> createDistance(@RequestBody DistanceEntity distanceEntity) {
        DistanceEntity createdDistance = distanceService.createDistance(distanceEntity);
        return ResponseEntity.ok(createdDistance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistanceEntity> updateDistance(@PathVariable Long id, @RequestBody DistanceEntity distanceEntity) {
        DistanceEntity updatedDistance = distanceService.updateDistance(id, distanceEntity);
        if (updatedDistance != null) {
            return ResponseEntity.ok(updatedDistance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DistanceEntity> deleteDistance(@PathVariable Long id) {
        boolean deleted = distanceService.deleteDistance(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
