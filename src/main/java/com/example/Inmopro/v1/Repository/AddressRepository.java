package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Geography.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Métodos de consulta personalizados pueden ser añadidos aquí si es necesario
}
