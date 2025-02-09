package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Users.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
}
