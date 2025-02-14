package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Request.Request;
import com.example.Inmopro.v1.Model.Request.RequestStatus;
import com.example.Inmopro.v1.Model.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Optional<Request> findByRequestId(Integer requestId);

    boolean existsByTenantAndStatusId(Users tenant, RequestStatus status);

    @Query("SELECT r.requestId, t.name, t.email, s.statusName, rt.requestTypeName, r.description, r.createdAt, r.updatedAt " +
            "FROM Request r " +
            "INNER JOIN r.tenant t " +
            "INNER JOIN r.statusId s " +
            "INNER JOIN r.requestTypeId rt")
    Optional<Object[]> findAllRequests();

    @Query("SELECT r.requestId, t.name, t.email, s.statusName, rt.requestTypeName, r.description, r.createdAt, r.updatedAt " +
            "FROM Request r " +
            "INNER JOIN r.tenant t " +
            "INNER JOIN r.statusId s " +
            "INNER JOIN r.requestTypeId rt " +
            "WHERE r.requestId = :requestId")
    Optional<Object[]> findRequestById(@Param("requestId") Integer requestId);

    @Query("SELECT r FROM Request r " +
            "JOIN r.tenant u " +
            "JOIN Property p ON p.idTenant.user_id = u.user_id " +
            "JOIN p.idMonitor m " +
            "WHERE m.user_id = :monitorId")
    Optional<Object[]> findAllRequestsByRol(@Param("monitorId") Integer monitorId);

    @Query("SELECT r FROM Request r " +
            "JOIN r.tenant u " +
            "JOIN Property p ON p.idTenant.user_id = u.user_id " +
            "JOIN p.idMonitor m " +
            "WHERE r.requestId = :requestId AND m.user_id = :monitorId")
    Optional<Object[]> findByIdAndMonitorId(Integer requestId, Integer monitorId);
    @Query("SELECT r FROM Request r " +
            "JOIN r.tenant u " +
            "JOIN Property p ON p.idTenant.user_id = u.user_id " +
            "JOIN p.idMonitor m " +
            "WHERE m.user_id = :monitorId AND r.statusId.id = :statusRequestId")
    Optional<Object[]> findAllRequestsByRolAndPending(@Param("monitorId") Integer monitorId, @Param("statusRequestId") Integer statusRequestId);

    //Optional<Object[]> findAllRequestsByRolAndPending(@Param("monitorId") Integer monitorId, @Param("statusRequest")Integer statusRequestId );
}
