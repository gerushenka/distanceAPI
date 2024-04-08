package com.example.distance.repository;

import com.example.distance.entity.City;
import com.example.distance.entity.Distance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DistanceRepository extends CrudRepository<Distance, Long> {
  Optional<Distance> findByCityDistance(double cityDistance);

  List<Distance> findByCityFirstOrCitySecond(City cityFirst, City citySecond);

  @Query("SELECT d FROM Distance d WHERE "
      + "d.cityFirst.name = :cityFirst AND "
      + "d.citySecond.name = :citySecond")
  List<Distance> findByCityNames(String cityFirst, String citySecond);
}
