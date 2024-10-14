package com.example.Inmopro.v1.Repository;

import com.example.Inmopro.v1.Model.Geography.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    // Puedes agregar métodos de consulta personalizados si lo necesitas
}
