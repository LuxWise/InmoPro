package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
}
