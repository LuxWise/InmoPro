package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Optional<Request> findByRequestId(Integer requestId);
}
