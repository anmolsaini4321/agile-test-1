package com.hrms.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "user_id")
    private String userId;

    private String text;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Optional: for future nested replies
    private String parentId;

}
