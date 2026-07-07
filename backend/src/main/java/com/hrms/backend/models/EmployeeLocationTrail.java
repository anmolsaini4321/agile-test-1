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
@Table(name = "employee_location_trails")
public class EmployeeLocationTrail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(name = "employee_id")
    public String employeeId;

    @Column(name = "attendance_id")
    public String attendanceId;

    public Double latitude;
    public Double longitude;
    
    @Column(name = "recorded_at")
    public Instant recordedAt;
}
