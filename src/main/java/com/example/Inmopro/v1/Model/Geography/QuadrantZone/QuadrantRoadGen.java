package com.example.Inmopro.v1.Model.Geography.QuadrantZone;

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
@Table(name = "QuadrantRoadGen") // Nombre de la tabla en la base de datos
public class QuadrantRoadGen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quadrantRoadGen", nullable = false)
    private String quadrantRoadGen;

    @Column(name = "visible", nullable = false)
    private Boolean visible;
}

