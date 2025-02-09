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
@Table(name = "TypeRoad")
public class TypeRoad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ajusta según tu estrategia de ID
    private Long id;

    @Column(name = "typeRoad")
    private String typeRoad;

    @Column(name = "visible")
    private Boolean visible;
}

