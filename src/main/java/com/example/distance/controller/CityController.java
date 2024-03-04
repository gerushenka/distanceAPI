package com.example.distance.controller;

import com.example.distance.entity.CityEntity;
import com.example.distance.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<CityEntity> createCity(@RequestBody CityEntity cityEntity) {
        CityEntity createdCity = cityService.createCity(cityEntity);
        return ResponseEntity.ok(createdCity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityEntity> getCity(@PathVariable Long id) {
        return cityService.getCityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityEntity> updateCity(@PathVariable Long id, @RequestBody CityEntity cityEntity) {
        CityEntity updatedCity = cityService.updateCity(id, cityEntity);
        if (updatedCity != null) {
            return ResponseEntity.ok(updatedCity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CityEntity> deleteCity(@PathVariable Long id) {
        boolean deleted = cityService.deleteCityAndRelatedDistances(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
