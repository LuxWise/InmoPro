package com.example.Inmopro.v1.Model.Request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "followuprequest")
public class FollowUpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request requestId;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private RequestStatus statusId;

    @Column(name = "in_force", nullable = false)
    private boolean inForce;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
