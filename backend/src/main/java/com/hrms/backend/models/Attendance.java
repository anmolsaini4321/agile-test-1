package com.hrms.backend.models;

import com.hrms.backend.models.enums.AttendanceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "attendances")
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(name = "employee_id")
    public String employee;
    public String companyCode;

    @Enumerated(EnumType.STRING)
    public AttendanceType type; // "CHECKIN" or "CHECKOUT"
    public String latitude;
    public String longitude;
    public Instant checkIn;
    public Instant checkOut;
}
