package com.example.distance.repository;

import com.example.distance.entity.DistanceCityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistanceCityRepo extends CrudRepository<DistanceCityEntity, Long> {
    // Добавьте дополнительные методы, если необходимо
}