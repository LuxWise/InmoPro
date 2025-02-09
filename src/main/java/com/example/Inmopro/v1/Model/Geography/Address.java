package com.example.Inmopro.v1.Model.Geography;

import com.example.Inmopro.v1.Model.Geography.QuadrantZone.*;
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
@Table(name = "Address") // Nombre de la tabla en la base de datos
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_type_road", nullable = false)
    private TypeRoad idTypeRoad; 

    @Column(name = "number_road", nullable = false)
    private Integer numberRoad;

    @ManyToOne // Relación con Quadrant (o cualquier otra entidad relevante)
    @JoinColumn(name = "id_quadrant", nullable = false)
    private Quadrant quadrant;

    @Column(name = "number_road_gen", nullable = false)
    private Integer numberRoadGen;

    @ManyToOne // Relación con QuadrantRoadGen (o cualquier otra entidad relevante)
    @JoinColumn(name = "id_quadrant_road_gen", nullable = false)
    private QuadrantRoadGen quadrantRoadGen;

    @Column(name = "plate_number", nullable = false)
    private Integer plateNumber;

    @Column(name = "complement_data")
    String complementData;
}
