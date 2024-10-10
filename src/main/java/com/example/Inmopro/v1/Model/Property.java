package com.example.Inmopro.v1.Model;

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
@Table(name = "Property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    private double latitude;
    private double longitude;

    @Column(name = "id_address")
    private Long idAddress;

    @Column(name = "id_tenant")
    private Long idTenant;

    @Column(name = "id_owner")
    private Long idOwner;

    @Column(name = "id_monitor")
    private Long idMonitor;

    @Column(name = "id_city")
    private Long idCity;

    @Column(name = "id_zone")
    private Long idZone;

}
