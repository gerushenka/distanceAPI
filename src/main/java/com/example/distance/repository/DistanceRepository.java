package com.example.distance.repository;

import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistanceRepository extends CrudRepository<Distance, Long>{
    Optional<Distance> findByCityDistance(double cityDistance);
    List<Distance> findByCity1OrCity2(City city1, City city2);

    @Query("SELECT d FROM Distance d WHERE d.city1.name = :city1Name AND d.city2.name = :city2Name")
    List<Distance> findByCityNames(String city1Name, String city2Name);
}
