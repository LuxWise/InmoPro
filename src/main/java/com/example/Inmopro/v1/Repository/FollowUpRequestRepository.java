package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Request.FollowUpRequest;
import com.example.Inmopro.v1.Model.Request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowUpRequestRepository extends JpaRepository<FollowUpRequest, UUID> {
    List<FollowUpRequest> findByRequestId(Request request);

    @Query("SELECT f.id, s.statusName, s.description, f.inForce, f.createdAt " +
            "FROM FollowUpRequest f JOIN f.statusId s WHERE s.statusName = :statusName")
    List<Object[]> findByStatusName(@Param("statusName") String statusName);

}


