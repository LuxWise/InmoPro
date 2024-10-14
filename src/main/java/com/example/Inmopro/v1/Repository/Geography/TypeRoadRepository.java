package com.example.Inmopro.v1.Repository.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.TypeRoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRoadRepository extends JpaRepository<TypeRoad, Long> {

}
