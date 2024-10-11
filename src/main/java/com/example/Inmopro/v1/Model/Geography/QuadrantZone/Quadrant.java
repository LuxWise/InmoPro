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
@Table(name = "Quadrant") // Nombre de la tabla en la base de datos
public class Quadrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "letter", nullable = false)
    private Character letter;

    @Column(name = "quadrant", nullable = false)
    private String quadrant;

    @Column(name = "visible", nullable = false)
    private Boolean visible;
}
