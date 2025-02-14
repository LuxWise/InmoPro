package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {
    Optional<RequestStatus> findById(Integer requestStatus);
}
