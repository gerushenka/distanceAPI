package com.example.distance.controller;


import com.example.distance.entity.DistanceCityEntity;
import com.example.distance.model.distanceCityDTO.DistanceCityDTO;
import com.example.distance.repository.DistanceCityRepo;
import com.example.distance.service.DistanceCityService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/distance-cities")
public class DistanceCityController {
    private final DistanceCityService distanceCityService;
    private final ModelMapper modelMapper;
    private final DistanceCityRepo distanceCityRepo;
    public DistanceCityController(DistanceCityService distanceCityService, ModelMapper modelMapper, DistanceCityRepo distanceCityRepo) {
        this.distanceCityService = distanceCityService;
        this.modelMapper = modelMapper;
        this.distanceCityRepo = distanceCityRepo;
    }

    @PostMapping
    public ResponseEntity<DistanceCityDTO> createDistanceCity(@RequestBody DistanceCityDTO distanceCityDTO) {
        DistanceCityDTO createdDistanceCityDTO = distanceCityService.createDistanceCity(distanceCityDTO);
        return ResponseEntity.ok(createdDistanceCityDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<DistanceCityDTO>> getDistanceCityById(@PathVariable Long id) {
        Optional<DistanceCityDTO> distanceCityOptional = distanceCityService.getDistanceCityById(id);
        return ResponseEntity.ok(distanceCityOptional);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistanceCityDTO> updateDistanceCity(@PathVariable Long id, @RequestBody DistanceCityDTO distanceCityDTO) {
        distanceCityDTO.setId(id);
        DistanceCityDTO updatedDistanceCityDTO = distanceCityService.updateDistanceCity(id, distanceCityDTO);
        return updatedDistanceCityDTO != null ? ResponseEntity.ok(updatedDistanceCityDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDistanceCity(@PathVariable Long id) {
        boolean deleted = distanceCityService.deleteDistanceCity(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}