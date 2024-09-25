package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowUpRequestRepository extends JpaRepository<FollowUpRequest, UUID> {
    List<FollowUpRequest> findByRequestId(Request request);
}


