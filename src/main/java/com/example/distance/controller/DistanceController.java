package com.example.distance.controller;

import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.model.dtodistance.DistanceDTO;
import com.example.distance.service.CityService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/distance")
public class DistanceController {

    @Autowired
    private Map<String, Double> distanceCache;

    @Value("${geoname.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final DistanceService distanceService;
    private final CityService cityService;

    private static final Logger logger = LoggerFactory.getLogger(DistanceController.class);

    public DistanceController(RestTemplate restTemplate, DistanceService distanceService, CityService cityService) {
        this.restTemplate = restTemplate;
        this.distanceService = distanceService;
        this.cityService = cityService;
    }

    @GetMapping("/calculate")
    public ResponseEntity calculateDistance(@RequestParam String cityFirst, @RequestParam String citySecond) {

        String url1 = "http://api.geonames.org/searchJSON?q=" + cityFirst + "&maxRows=1&username=" + apiKey;
        String url2 = "http://api.geonames.org/searchJSON?q=" + citySecond + "&maxRows=1&username=" + apiKey;
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

            City city1 = cityService.saveCity(cityFirst, lat1, lng1);
            City city2 = cityService.saveCity(citySecond, lat2, lng2);

            double distance = distanceService.calculateDistance(lat1, lng1, lat2, lng2);
            DecimalFormat df = new DecimalFormat("#.##");
            distanceService.saveDistance(distance, city1, city2);
            String formattedDistance = df.format(distance);
            ObjectNode responseJson = objectMapper.createObjectNode();
            responseJson.put("cityFirst", cityFirst);
            responseJson.put("citySecond", citySecond);
            responseJson.put("distance_km", formattedDistance);
            return ResponseEntity.ok(responseJson);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке JSON");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistanceDTO> getDistance(@PathVariable Long id) {
        Optional<Distance> distanceEntityOptional = distanceService.getDistanceById(id);
        if (distanceEntityOptional.isPresent()) {
            Distance distance = distanceEntityOptional.get();
            DistanceDTO distanceDTO = convertToDTO(distance);
            return ResponseEntity.ok(distanceDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private DistanceDTO convertToDTO(Distance distance) {
        DistanceDTO dto = new DistanceDTO();
        dto.setId(distance.getId());
        dto.setCityDistance(distance.getCityDistance());

        Optional<City> cityFirstOptional = cityService.getCityById(distance.getCity1().getId());
        Optional<City> citySecondOptional = cityService.getCityById(distance.getCity2().getId());

        if (cityFirstOptional.isPresent() && citySecondOptional.isPresent()) {
            String cityFirst = cityFirstOptional.get().getName();
            String citySecond = citySecondOptional.get().getName();

            dto.setCityFirst(cityFirst);
            dto.setCitySecond(citySecond);
        } else {

            dto.setCityFirst("Unknown");
            dto.setCitySecond("Unknown");
        }

        return dto;
    }

    @PostMapping
    public ResponseEntity<Distance> createDistance(@RequestBody Distance distance) {
        Distance createdDistance = distanceService.createDistance(distance);
        return ResponseEntity.ok(createdDistance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Distance> updateDistance(@PathVariable Long id, @RequestBody Distance distance) {
        Distance updatedDistance = distanceService.updateDistance(id, distance);
        if (updatedDistance != null) {
            return ResponseEntity.ok(updatedDistance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Distance> deleteDistance(@PathVariable Long id) {
        boolean deleted = distanceService.deleteDistance(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/useful")
    public ResponseEntity<List<DistanceDTO>> getDistancesByCityNames(@RequestParam List<String> cityFirstList, @RequestParam List<String> citySecondList) {
        if (cityFirstList.size() != citySecondList.size()) {
            return ResponseEntity.badRequest().build();
        }

        List<DistanceDTO> result = new ArrayList<>();

        for (int i = 0; i < cityFirstList.size(); i++) {
            String cityFirst = cityFirstList.get(i);
            String citySecond = citySecondList.get(i);
            String key = generateCacheKey(cityFirst, citySecond);

            if (distanceCache != null && distanceCache.containsKey(key)) {
                double distance = distanceCache.get(key);
                DistanceDTO dto = new DistanceDTO();
                dto.setCityFirst(cityFirst);
                dto.setCitySecond(citySecond);
                dto.setCityDistance(distance);
                result.add(dto);
            } else {
                List<Distance> distances = distanceService.getDistancesByCityNames(cityFirst, citySecond);
                if (!distances.isEmpty()) {
                    double distance = distances.get(0).getCityDistance();
                    if (distanceCache != null) {
                        distanceCache.put(key, distance);
                    }
                    viewCacheContents();
                }
                result.addAll(distances.stream()
                        .map(this::convertToDTO)
                        .toList());
            }
        }

        return ResponseEntity.ok(result);
    }


    private String generateCacheKey(String cityFirst, String citySecond) {
        return cityFirst + "_" + citySecond;
    }

    public void viewCacheContents() {
        logger.info("Cache Contents:");
        distanceCache.forEach((key, value) -> logger.info(String.format("%s : %s", key, value)));
    }

}
