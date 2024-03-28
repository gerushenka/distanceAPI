package com.example.distance.controller;

import com.example.distance.dto.CityDTO;
import com.example.distance.entity.City;
import com.example.distance.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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
     * @param cityDTO информация о городе
     * @return созданный город
     */
    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) {
        logger.info("Received POST request to /cities with cityDTO: {}", cityDTO);
        City createdCity = cityService.createCity(cityDTO);
        CityDTO createdCityDTO = convertToDTO(createdCity);
        return ResponseEntity.ok(createdCityDTO);
    }

    /**
     * Возвращает информацию о городе по его идентификатору.
     *
     * @param id идентификатор города
     * @return информация о городе
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable Long id) {
        logger.info("Received GET request to /cities/{}", id);
        Optional<City> cityOptional = cityService.getCityById(id);
        if (cityOptional.isPresent()) {
            CityDTO cityDTO = convertToDTO(cityOptional.get());
            return ResponseEntity.ok(cityDTO);
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

    private CityDTO convertToDTO(City city) {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        cityDTO.setLatitude(city.getLatitude());
        cityDTO.setLongitude(city.getLongitude());
        return cityDTO;
    }
}