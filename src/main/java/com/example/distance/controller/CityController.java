package com.example.distance.controller;

import com.example.distance.entity.City;
import com.example.distance.model.citydto.CityDataTransferObject;
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
    public ResponseEntity<CityDataTransferObject> createCity(@RequestBody CityDataTransferObject cityDataTransferObject) {
        City createdCity = cityService.createCity(cityDataTransferObject);
        CityDataTransferObject createdCityDataTransferObject = convertToDTO(createdCity);
        return ResponseEntity.ok(createdCityDataTransferObject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDataTransferObject> getCity(@PathVariable Long id) {
        Optional<City> cityOptional = cityService.getCityById(id);
        if (cityOptional.isPresent()) {
            CityDataTransferObject cityDataTransferObject = convertToDTO(cityOptional.get());
            return ResponseEntity.ok(cityDataTransferObject);
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

    private CityDataTransferObject convertToDTO(City city) {
        CityDataTransferObject cityDataTransferObject = new CityDataTransferObject();
        cityDataTransferObject.setId(city.getId());
        cityDataTransferObject.setName(city.getName());
        cityDataTransferObject.setLatitude(city.getLatitude());
        cityDataTransferObject.setLongitude(city.getLongitude());
        return cityDataTransferObject;
    }


}
