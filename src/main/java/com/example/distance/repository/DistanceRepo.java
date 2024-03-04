package com.example.distance.repository;

import com.example.distance.entity.DistanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistanceRepo extends CrudRepository<DistanceEntity, Long>{
    Optional<DistanceEntity> findByDistance(double distance);
}
