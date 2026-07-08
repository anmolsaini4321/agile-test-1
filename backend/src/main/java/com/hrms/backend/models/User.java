package com.hrms.backend.models;

import com.hrms.backend.models.enums.Department;
import com.hrms.backend.models.enums.Position;
import com.hrms.backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String email;

    private String phone;

    private String password;

    private Boolean isGoogleUser = false;

    private String gender;

    private java.time.LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String waitingCompanyCode;

    private String companyCode;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private Department department;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
