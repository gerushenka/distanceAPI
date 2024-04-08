package com.example.distance.controller;

import com.example.distance.config.CacheConfig;
import com.example.distance.dto.DistanceDto;
import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import com.example.distance.service.CityService;
import com.example.distance.service.DistanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/distance")
public class DistanceController {
  private static final int CACHESIZE = 5;

  private CacheConfig cache;


  @Value("${geoname.api-key}")
  private String apiKey;

  @Value("${geoname}")
  private String geoname;

  private final RestTemplate restTemplate;
  private final DistanceService distanceService;
  private final CityService cityService;

  private static final Logger logger = LoggerFactory.getLogger(DistanceController.class);

  public DistanceController(RestTemplate restTemplate, DistanceService distanceService,
                            CityService cityService, CacheConfig cache) {
    this.restTemplate = restTemplate;
    this.distanceService = distanceService;
    this.cityService = cityService;
    this.cache = cache;
  }

  @GetMapping("/calculate")
  public ResponseEntity<ObjectNode> calculateDistance(@RequestParam String cityFirst,
                                                      @RequestParam String citySecond) {
    if (cityFirst == null || citySecond == null) {
      return ResponseEntity.badRequest()
              .body(createErrorResponse("Both 'cityFirst' and 'citySecond' parameters are required"));
    }

    String url1 =
            "http://api.geonames.org/searchJSON?q=" + cityFirst + "&maxRows=1&username=" + apiKey;
    String url2 =
            "http://api.geonames.org/searchJSON?q=" + citySecond + "&maxRows=1&username=" + apiKey;
    String jsonResponse1 = restTemplate.getForObject(url1, String.class);
    String jsonResponse2 = restTemplate.getForObject(url2, String.class);

    try {
      ObjectMapper objectMapper = new ObjectMapper();

      JsonNode node1 = objectMapper.readTree(jsonResponse1);
      JsonNode node2 = objectMapper.readTree(jsonResponse2);

      if (node1.has(geoname) && node2.has(geoname)) {
        JsonNode geonames1 = node1.get(geoname);
        JsonNode geonames2 = node2.get(geoname);

        if (geonames1.isArray() && geonames2.isArray() && geonames1.size() > 0
                && geonames2.size() > 0) {
          double lat1 = Double.parseDouble(geonames1.get(0).path("lat").asText());
          double lng1 = Double.parseDouble(geonames1.get(0).path("lng").asText());
          double lat2 = Double.parseDouble(geonames2.get(0).path("lat").asText());
          double lng2 = Double.parseDouble(geonames2.get(0).path("lng").asText());

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
        }
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(createErrorResponse("Error parsing JSON response: geonames array is missing or empty"));
    } catch (JsonProcessingException e) {
      return ResponseEntity.badRequest().body(createErrorResponse("Error processing JSON"));
    } catch (NumberFormatException e) {
      return ResponseEntity.badRequest().body(createErrorResponse("Error parsing latitude or longitude"));
    }
  }
  private ObjectNode createErrorResponse(String message) {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode errorResponse = objectMapper.createObjectNode();
    errorResponse.put("error", message);
    return errorResponse;
  }
  @GetMapping("/{id}")
  public ResponseEntity<DistanceDto> getDistance(@PathVariable Long id) {
    logger.info("Received GET request to /distance/{}", id);
    Optional<Distance> distanceEntityOptional = distanceService.getDistanceById(id);
    if (distanceEntityOptional.isPresent()) {
      Distance distance = distanceEntityOptional.get();
      DistanceDto distanceDto = convertToDto(distance);
      return ResponseEntity.ok(distanceDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  private DistanceDto convertToDto(Distance distance) {
    DistanceDto dto = new DistanceDto();
    dto.setId(distance.getId());
    dto.setCityDistance(distance.getCityDistance());

    Optional<City> cityFirstOptional = cityService.getCityById(distance.getCityFirst().getId());
    Optional<City> citySecondOptional = cityService.getCityById(distance.getCitySecond().getId());

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
    logger.info("Received POST request to /distance with body: {}", distance);
    Distance createdDistance = distanceService.createDistance(distance);
    return ResponseEntity.ok(createdDistance);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Distance> updateDistance(@PathVariable Long id,
                                                 @RequestBody Distance distance) {
    logger.info("Received PUT request to /distance/{} with body: {}", id, distance);
    Distance updatedDistance = distanceService.updateDistance(id, distance);
    if (updatedDistance != null) {
      return ResponseEntity.ok(updatedDistance);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Distance> deleteDistance(@PathVariable Long id) {
    logger.info("Received DELETE request to /distance/{}", id);
    boolean deleted = distanceService.deleteDistance(id);
    if (deleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/city-to-city")
  public ResponseEntity<List<DistanceDto>> getDistancesByCityNames(
      @RequestParam List<String> cityFirstList, @RequestParam List<String> citySecondList) {
    if (cityFirstList.size() != citySecondList.size()) {
      return ResponseEntity.badRequest().build();
    }

    List<DistanceDto> result = new ArrayList<>();
    Map<String, Double> distanceCache = cache.distanceCache();

    for (int i = 0; i < cityFirstList.size(); i++) {
      String cityFirst = cityFirstList.get(i);
      String citySecond = citySecondList.get(i);
      String key = generateCacheKey(cityFirst, citySecond);

      if (distanceCache.containsKey(key)) {
        double distance = distanceCache.get(key);
        DistanceDto dto = new DistanceDto();
        dto.setCityFirst(cityFirst);
        dto.setCitySecond(citySecond);
        dto.setCityDistance(distance);
        result.add(dto);
      } else {
        List<Distance> distances = distanceService.getDistancesByCityNames(cityFirst, citySecond);
        if (distances.isEmpty()) {
          City city1 = cityService.saveCity(cityFirst, 0, 0);
          City city2 = cityService.saveCity(citySecond, 0, 0);
          double distance =
              distanceService.calculateDistance(city1.getLatitude(), city1.getLongitude(),
                  city2.getLatitude(), city2.getLongitude());
          Distance newDistance = distanceService.saveDistance(distance, city1, city2);
          distances.add(newDistance);
        }
        for (Distance distance : distances) {
          cache.putWithEviction(key, distance.getCityDistance(), CACHESIZE);
        }

        result.addAll(distances.stream()
            .map(this::convertToDto)
            .toList());
      }
    }

    viewCacheContents(distanceCache);
    return ResponseEntity.ok(result);
  }

  private String generateCacheKey(String cityFirst, String citySecond) {
    return cityFirst + "_" + citySecond;
  }

  public void viewCacheContents(Map<String, Double> distanceCache) {
    logger.info("Cache Contents:");
    distanceCache.forEach((key, value) -> logger.info(String.format("%s : %s", key, value)));
  }
}
