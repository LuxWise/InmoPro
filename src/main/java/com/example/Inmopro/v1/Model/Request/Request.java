package com.example.Inmopro.v1.Model.Request;

import com.example.Inmopro.v1.Model.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;

    @OneToOne
    @JoinColumn( name = "tenant_id", nullable = false)
    private Users tenant;

    @OneToOne
    @JoinColumn(name = "request_type_id", nullable = false)
    private RequestType requestTypeId;

    @ManyToOne
    @JoinColumn(name = "status_id" , nullable = false)
    private RequestStatus statusId;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
