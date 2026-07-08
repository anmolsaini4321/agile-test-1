package com.hrms.backend.models;

import jakarta.persistence.*;
import lombok.*;

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
}
