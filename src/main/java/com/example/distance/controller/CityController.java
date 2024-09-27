package com.example.distance.controller;

import com.example.distance.dto.CityDto;
import com.example.distance.entity.City;
import com.example.distance.service.CityService;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
public class CityController {

  private static final Logger logger = LoggerFactory.getLogger(CityController.class);

  private final CityService cityService;


  public CityController(CityService cityService) {
    this.cityService = cityService;
  }

  /**
   * Создает новый город.
   *
   * @param cityDto информация о городе
   * @return созданный город
   */
  @PostMapping
  public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
    logger.info("Received POST request to /cities with cityDto: {}", cityDto);
    City createdCity = cityService.createCity(cityDto);
    CityDto createdCityDto = convertToDto(createdCity);
    return ResponseEntity.ok(createdCityDto);
  }

  /**
   * Возвращает информацию о городе по его идентификатору.
   *
   * @param id идентификатор города
   * @return информация о городе
   */
  @GetMapping("/{id}")
  public ResponseEntity<CityDto> getCity(@PathVariable Long id) {
    logger.info("Received GET request to /cities/{}", id);
    Optional<City> cityOptional = cityService.getCityById(id);
    if (cityOptional.isPresent()) {
      CityDto cityDto = convertToDto(cityOptional.get());
      return ResponseEntity.ok(cityDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Обновляет информацию о городе.
   *
   * @param id   идентификатор города
   * @param city информация о городе
   * @return обновленная информация о городе
   */
  @PutMapping("/{id}")
  public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City city) {
    logger.info("Received PUT request to /cities/{} with city: {}", id, city);
    City updatedCity = cityService.updateCity(id, city);
    if (updatedCity != null) {
      return ResponseEntity.ok(updatedCity);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Удаляет город по его идентификатору.
   *
   * @param id идентификатор города
   * @return статус операции удаления
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<City> deleteCity(@PathVariable Long id) {
    logger.info("Received DELETE request to /cities/{}", id);
    boolean deleted = cityService.deleteCityAndRelatedDistances(id);
    if (deleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  private CityDto convertToDto(City city) {
    CityDto cityDto = new CityDto();
    cityDto.setId(city.getId());
    cityDto.setName(city.getName());
    cityDto.setLatitude(city.getLatitude());
    cityDto.setLongitude(city.getLongitude());
    return cityDto;
  }

  @PostMapping("/bulk")
  public ResponseEntity<List<CityDto>> createCities(@RequestBody List<CityDto> cityDtos) {
    logger.info("Received POST request to /cities/bulk with cityDtos: {}", cityDtos);

    List<City> createdCities = cityDtos.stream()
            .map(cityService::createCity)
            .toList();

    List<CityDto> createdCityDtos = createdCities.stream()
            .map(this::convertToDto)
            .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(createdCityDtos);
  }

}