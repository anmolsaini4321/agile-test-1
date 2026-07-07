package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Company.SuperAdminCompanyDto;
import com.hrms.backend.models.Company;
import com.hrms.backend.models.User;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/super-admin")
@CrossOrigin(origins = "*")
public class SuperAdminController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    // In-memory cache for status and features since database schema ddl-auto=none
    private static final Map<String, String> companyStatusMap = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> companyFeaturesMap = new ConcurrentHashMap<>();

    @GetMapping("/companies")
    public ResponseEntity<List<SuperAdminCompanyDto>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        List<SuperAdminCompanyDto> dtos = new ArrayList<>();

        for (Company company : companies) {
            String status = companyStatusMap.computeIfAbsent(company.getId(), id -> "APPROVED");
            Set<String> features = companyFeaturesMap.computeIfAbsent(company.getId(), id -> 
                new HashSet<>(Arrays.asList("ATTENDANCE", "LEAVES", "TASKS", "MEETINGS", "CHAT"))
            );

            String hrName = "System";
            String hrEmail = "admin@system.com";

            if (company.getHr() != null) {
                Optional<User> hrUser = userRepository.findById(company.getHr());
                if (hrUser.isPresent()) {
                    hrName = hrUser.get().getName();
                    hrEmail = hrUser.get().getEmail();
                }
            }

            String createdDateStr = company.getCreatedDate() != null ? 
                    company.getCreatedDate().toString().replace("T", " ").substring(0, 16) : 
                    "2026-07-01 00:00";

            SuperAdminCompanyDto dto = SuperAdminCompanyDto.builder()
                    .id(company.getId())
                    .companyCode(company.getCompanyCode())
                    .companyName(company.getCompanyName())
                    .adminName(hrName)
                    .adminEmail(hrEmail)
                    .status(status)
                    .allowedFeatures(features)
                    .createdAt(createdDateStr)
                    .build();

            dtos.add(dto);
        }

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/companies/{id}/status")
    public ResponseEntity<Void> updateCompanyStatus(
            @PathVariable("id") String companyId,
            @RequestParam("status") String status
    ) {
        companyStatusMap.put(companyId, status.toUpperCase());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/companies/{id}/features")
    public ResponseEntity<Void> updateCompanyFeatures(
            @PathVariable("id") String companyId,
            @RequestBody Set<String> features
    ) {
        companyFeaturesMap.put(companyId, features);
        return ResponseEntity.ok().build();
    }
}
