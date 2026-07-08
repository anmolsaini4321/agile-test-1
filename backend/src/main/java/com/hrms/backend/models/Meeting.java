package com.hrms.backend.models;

import com.hrms.backend.models.enums.MeetingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    @Column(name = "organizer_id")
    private String organizer; // User who created the meeting // HR
    private String companyCode;
    private String meetingLink;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "meeting_participants", joinColumns = @JoinColumn(name = "meeting_id"))
    @Column(name = "participant_id")
    private Set<String> participants; // employees

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "meeting_responses", joinColumns = @JoinColumn(name = "meeting_id"))
    private List<MeetingResponse> responses; // status of each participant

    @Enumerated(EnumType.STRING)
    private MeetingStatus status = MeetingStatus.SCHEDULED; // SCHEDULED, CANCELLED, COMPLETED
}
