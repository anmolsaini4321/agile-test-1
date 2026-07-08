package com.hrms.backend.dtos.entityDtos.User.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateProfileRequestDto {

    @Size(min = 3,max = 50,message = "Name should be between 3 to 50 character")
    private String name;

    @Pattern(
            regexp = "^\\+\\d{1,4}\\d{10}$",
            message = "Phone number must start with a country code (e.g., +91) followed by exactly 10 digits"
    )
    private String phone;

    @Pattern(regexp = "^$|M|F", message = "Gender must be M or F or blank")
    private String gender;

    @Pattern(
            regexp = "^$|INTERN|JUNIOR_DEVELOPER|SENIOR_DEVELOPER|TEAM_LEAD|MANAGER|HR|CTO|CEO|OTHERS",
            message = "Position must be one of: INTERN, JUNIOR_DEVELOPER, SENIOR_DEVELOPER, TEAM_LEAD, MANAGER, HR, CTO, CEO, OTHERS"
    )
    private String position;

    @Pattern(
            regexp = "^$|HR|ENGINEERING|SALES|MARKETING|FINANCE|OPERATIONS|ADMINISTRATION|SUPPORT|OTHERS",
            message = "Department must be one of: HR, ENGINEERING, SALES, MARKETING, FINANCE, OPERATIONS, ADMINISTRATION, SUPPORT, OTHERS"
    )
    private String department;

    private String aadhar;
    private String maritalStatus;
    private String bloodGroup;
    private String physicallyChallenged;
    private String currentAddress;
    private String permanentAddress;
    private String fathersName;
    private String mothersName;
    private String emergencyName;
    private String emergencyNumber;
    private String emergencyRelation;
    private String bankName;
    private String accountHolder;
    private String accountNumber;
    private String ifscCode;
    private String upiId;
    private String uan;
    private String pan;
    private String pfNumber;
    private String pfJoining;
    private String esiNumber;
    private String esiJoining;
    private String epsNumber;
    private String epsExit;
}
