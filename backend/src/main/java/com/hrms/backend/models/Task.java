package com.hrms.backend.models;

import com.hrms.backend.models.enums.Priority;
import com.hrms.backend.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String imageUrl;

    private String companyCode;

    private String title;

    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_STARTED; //By default NotStarted

    @Column(name = "assignee_id")
    private String assignee; //HR id

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "task_employees", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "employee_id")
    private Set<String> employees;

}
