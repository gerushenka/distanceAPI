package com.example.distance.service;

import com.example.distance.entity.DistanceCityEntity;
import com.example.distance.model.distancecitydto.DistanceCityDTO;
import com.example.distance.repository.DistanceCityRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DistanceCityService {
    private final DistanceCityRepo distanceCityRepo;
    private final ModelMapper modelMapper;

    public DistanceCityService(DistanceCityRepo distanceCityRepo, ModelMapper modelMapper) {
        this.distanceCityRepo = distanceCityRepo;
        this.modelMapper = modelMapper;
    }

    public DistanceCityDTO createDistanceCity(DistanceCityDTO distanceCityDTO) {
        DistanceCityEntity distanceCityEntity = modelMapper.map(distanceCityDTO, DistanceCityEntity.class);
        distanceCityEntity = distanceCityRepo.save(distanceCityEntity);
        return modelMapper.map(distanceCityEntity, DistanceCityDTO.class);
    }

    public Optional<DistanceCityDTO> getDistanceCityById(Long id) {
        Optional<DistanceCityEntity> distanceCityOptional = distanceCityRepo.findById(id);
        return distanceCityOptional.map(distanceCityEntity -> modelMapper.map(distanceCityEntity, DistanceCityDTO.class));
    }

    public DistanceCityDTO updateDistanceCity(Long id, DistanceCityDTO updatedDistanceCityDTO) {
        if (distanceCityRepo.existsById(id)) {
            updatedDistanceCityDTO.setId(id);
            DistanceCityEntity updatedDistanceCityEntity = modelMapper.map(updatedDistanceCityDTO, DistanceCityEntity.class);
            updatedDistanceCityEntity = distanceCityRepo.save(updatedDistanceCityEntity);
            return modelMapper.map(updatedDistanceCityEntity, DistanceCityDTO.class);
        } else {
            return null;
        }
    }

    public boolean deleteDistanceCity(Long id) {
        if (distanceCityRepo.existsById(id)) {
            distanceCityRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}