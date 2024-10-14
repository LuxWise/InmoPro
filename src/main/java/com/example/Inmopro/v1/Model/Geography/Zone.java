package com.example.Inmopro.v1.Model.Geography;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Zone") // Nombre de la tabla en la base de datos
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zone", nullable = false)
    private String zone;

    @Column(name = "visible", nullable = false)
    private Boolean visible;
}

