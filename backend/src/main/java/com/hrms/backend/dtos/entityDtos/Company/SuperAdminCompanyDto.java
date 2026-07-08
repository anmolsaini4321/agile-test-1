package com.hrms.backend.dtos.entityDtos.Company;

import lombok.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminCompanyDto {
    private String id;
    private String companyCode;
    private String companyName;
    private String adminName;
    private String adminEmail;
    private String adminId;
    private String status;
    private Set<String> allowedFeatures;
    private String createdAt;
}
