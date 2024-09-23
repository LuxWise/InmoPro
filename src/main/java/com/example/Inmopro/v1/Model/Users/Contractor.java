package com.example.Inmopro.v1.Model.Users;

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
@Table(name = "Contractor")
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer contractor_id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Column(nullable = false)
    String document;

    @OneToOne
    @JoinColumn(name = "id_day", nullable = false)
    Day day;

    @OneToOne
    @JoinColumn(name = "id_vehicleStatus", nullable = false)
    vehicleStatus vehicleStatus;

    @OneToOne
    @JoinColumn(name = "id_ARL", nullable = false)
    ARL ARL;

    @OneToOne
    @JoinColumn(name = "id_epsStatus", nullable = false)
    EpsStatus status;

    @Column(nullable = false)
    boolean visible;
}
