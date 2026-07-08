package com.hrms.backend.dtos.entityDtos.User;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfo {
    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private String position;   // e.g. "SOFTWARE_ENGINEER" → displayed by frontend
    private String department; // e.g. "ENGINEERING"
}


