package com.example.distance.repository;

import com.example.distance.entity.CityEntity;
import com.example.distance.entity.DistanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistanceRepo extends CrudRepository<DistanceEntity, Long>{
    Optional<DistanceEntity> findByDistance(double distance);
    List<DistanceEntity> findByCity1OrCity2(CityEntity city1, CityEntity city2);

}
