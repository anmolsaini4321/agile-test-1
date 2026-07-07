package com.hrms.backend.models;

import com.hrms.backend.models.enums.LeaveStatus;
import com.hrms.backend.models.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "leave_requests")
public class LeaveRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "employee_id")
    private String userId; // employee

    private String companyCode;

    @Enumerated(EnumType.STRING)
    private LeaveType type;

    private String emergencyContact;

    private LocalDate startDate; // yyyy-MM-dd

    private LocalDate endDate; // yyyy-MM-dd

    private String leaveDescription;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;

    private LocalDateTime appliedAt = LocalDateTime.now();

    private LocalDateTime respondedAt; // approved or rejected

    private String respondedBy; // hr id when approved or rejected
}
