package com.example.Inmopro.v1.Model.Property;

import  com.example.Inmopro.v1.Model.Users.Users;
import  com.example.Inmopro.v1.Model.Geography.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_address", nullable = false)
    Address Address;

    @ManyToOne
    @JoinColumn(name = "id_tenant", nullable=false)
    Users idTenant;

    @ManyToOne
    @JoinColumn(name = "id_owner", nullable=false)
    Users idOwner;

    @ManyToOne
    @JoinColumn(name = "id_monitor", nullable=false)
    Users idMonitor;

    @ManyToOne
    @JoinColumn(name = "id_city", nullable=false)
    City idCity;

    @ManyToOne
    @JoinColumn(name = "id_zone", nullable=false)
    Zone idZone;

}
