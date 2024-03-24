package com.example.distance.controller;

import com.example.distance.entity.City;
import com.example.distance.dto.CityDTO;
import com.example.distance.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) {
        City createdCity = cityService.createCity(cityDTO);
        CityDTO createdCityDTO = convertToDTO(createdCity);
        return ResponseEntity.ok(createdCityDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable Long id) {
        Optional<City> cityOptional = cityService.getCityById(id);
        if (cityOptional.isPresent()) {
            CityDTO cityDTO = convertToDTO(cityOptional.get());
            return ResponseEntity.ok(cityDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City city) {
        City updatedCity = cityService.updateCity(id, city);
        if (updatedCity != null) {
            return ResponseEntity.ok(updatedCity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> deleteCity(@PathVariable Long id) {
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
