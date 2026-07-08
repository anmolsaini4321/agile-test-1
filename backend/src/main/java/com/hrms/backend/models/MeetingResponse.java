package com.hrms.backend.models;

import com.hrms.backend.models.enums.ResponseStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MeetingResponse {
    private String participant;
    
    @Enumerated(EnumType.STRING)
    private ResponseStatus status; // PENDING, ACCEPTED, DECLINED
}
