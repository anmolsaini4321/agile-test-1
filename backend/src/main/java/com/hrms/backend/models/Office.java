package com.hrms.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "offices")
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String companyCode;
    private String createdBy;
    private String latitude;
    private String longitude;
    private String radius;
    private Instant createdAt;
    private Instant updatedAt;
}
