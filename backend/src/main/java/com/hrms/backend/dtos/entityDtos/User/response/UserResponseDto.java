package com.hrms.backend.dtos.entityDtos.User.response;

import com.hrms.backend.models.enums.Department;
import com.hrms.backend.models.enums.Position;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String id;

    private String name;

    private String email;

    private String phone;

    private String gender;

    private String role;

    private Position position;

    private Department department;

    private String waitingCompanyCode;

    //only for employee, for hr we will assign it auto.
    private String companyCode;

    private String imageUrl;

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
