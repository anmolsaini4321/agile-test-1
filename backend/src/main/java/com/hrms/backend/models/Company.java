package com.hrms.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String companyCode;

    private String companyName;

    @Column(name = "created_date")
    private java.time.LocalDateTime createdDate;

    @Column(name = "hr_id")
    private String hr;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_employees", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "employee_id")
    private Set<String> employees = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_waitlist", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "employee_id")
    private Set<String> waitListEmployees = new HashSet<>(); // employee that HR does not still confirm

    /** Approval status: PENDING | APPROVED | SUSPENDED */
    @Column(name = "status")
    private String status = "PENDING";

    /** Comma-separated feature flags e.g. "ATTENDANCE,LEAVES,TASKS" */
    @Column(name = "allowed_features", columnDefinition = "TEXT")
    private String allowedFeaturesStr;

    public Set<String> getAllowedFeaturesAsSet() {
        if (allowedFeaturesStr == null || allowedFeaturesStr.isBlank()) {
            return new HashSet<>(Arrays.asList("ATTENDANCE", "LEAVES", "TASKS", "MEETINGS", "CHAT"));
        }
        return new HashSet<>(Arrays.asList(allowedFeaturesStr.split(",")));
    }

    public void setAllowedFeaturesFromSet(Set<String> features) {
        this.allowedFeaturesStr = (features != null && !features.isEmpty())
                ? String.join(",", features) : "";
    }
}
