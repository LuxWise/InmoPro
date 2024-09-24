package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface FollowUpRequestRepository extends JpaRepository<FollowUpRequest, UUID> {
    Optional<FollowUpRequest> findByRequestId(Integer requestId);
}

