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
@Table(name = "Day")
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer contractor_id;

    @Column(nullable = false)
    String day;

    @Column(nullable = false)
    boolean visible;

}
