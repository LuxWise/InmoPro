package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.User.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
}
