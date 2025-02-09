package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Dto.User.SearchRole;
import com.example.Inmopro.v1.Model.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    /*@Query("SELECT u FROM SearchRole r " +
            "JOIN r.tenant u " +
            "JOIN Property p ON p.idTenant.user_id = u.user_id " +
            "JOIN p.idMonitor m " +
            "WHERE r.requestId = :requestId AND m.user_id = :monitorId")
    Optional<SearchRole> findByEmailRol(String email);*/

    @Query("SELECT SearchRole(u.role,u.user_id) " +
            "FROM Users u " +
            "JOIN u.role r " +
            "WHERE u.user_id = :user_id")
    SearchRole findUserRoleById(@Param("user_id") Long userId);
}
