package com.example.distance.repository;

import com.example.distance.entity.CityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends CrudRepository<CityEntity, Long> {
    CityEntity findByName(String name);
}
