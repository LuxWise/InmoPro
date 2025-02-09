package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Request.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestTypeRepository extends JpaRepository<RequestType, Integer> {
    Optional<RequestType> findById(Integer requestType);
}
