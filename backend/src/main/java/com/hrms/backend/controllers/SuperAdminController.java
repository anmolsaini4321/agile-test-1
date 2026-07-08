package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Company.SuperAdminCompanyDto;
import com.hrms.backend.models.Attendance;
import com.hrms.backend.models.Company;
import com.hrms.backend.models.User;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/super-admin")
@CrossOrigin(origins = "*")
public class SuperAdminController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.hrms.backend.repositories.AttendanceRepository attendanceRepository;

    @GetMapping("/companies")
    public ResponseEntity<List<SuperAdminCompanyDto>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        List<SuperAdminCompanyDto> dtos = new ArrayList<>();

        for (Company company : companies) {
            String status = company.getStatus() != null ? company.getStatus() : "PENDING";
            Set<String> features = company.getAllowedFeaturesAsSet();

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
                    .adminId(company.getHr())
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
        companyRepository.findById(companyId).ifPresent(company -> {
            company.setStatus(status.toUpperCase());
            companyRepository.save(company);
        });
        return ResponseEntity.ok().build();
    }

    @PostMapping("/companies/{id}/features")
    public ResponseEntity<Void> updateCompanyFeatures(
            @PathVariable("id") String companyId,
            @RequestBody Set<String> features
    ) {
        companyRepository.findById(companyId).ifPresent(company -> {
            company.setAllowedFeaturesFromSet(features);
            companyRepository.save(company);
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping("/companies/{companyCode}/attendances")
    public ResponseEntity<List<Map<String, Object>>> getCompanyAttendances(
            @PathVariable("companyCode") String companyCode,
            @RequestParam(name = "date", required = false) String dateStr
    ) {
        java.time.LocalDate date;
        if (dateStr == null || dateStr.isBlank()) {
            date = java.time.LocalDate.now(java.time.ZoneOffset.UTC);
        } else {
            date = java.time.LocalDate.parse(dateStr);
        }

        java.time.Instant startOfDay = date.atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        java.time.Instant endOfDay = date.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant().minusNanos(1);

        List<User> companyUsers = userRepository.findAllByCompanyCode(companyCode);

        List<Attendance> attendances = attendanceRepository.findByCompanyCodeAndCheckInBetween(companyCode, startOfDay, endOfDay);

        Map<String, Attendance> attendanceMap = new HashMap<>();
        for (Attendance attendance : attendances) {
            if (attendance.getEmployee() != null) {
                attendanceMap.put(attendance.getEmployee(), attendance);
            }
        }

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (User user : companyUsers) {
            Map<String, Object> record = new HashMap<>();
            record.put("userId", user.getId());
            record.put("name", user.getName());
            record.put("email", user.getEmail());
            record.put("role", user.getRole() != null ? user.getRole().name() : "ROLE_USER");

            Attendance attendance = attendanceMap.get(user.getId());
            if (attendance != null) {
                record.put("checkIn", attendance.getCheckIn() != null ? attendance.getCheckIn().toString() : null);
                record.put("checkOut", attendance.getCheckOut() != null ? attendance.getCheckOut().toString() : null);
                record.put("latitude", attendance.getLatitude());
                record.put("longitude", attendance.getLongitude());

                if (attendance.getCheckOut() != null) {
                    record.put("status", "Checked Out");
                } else if (attendance.getCheckIn() != null) {
                    record.put("status", "Checked In");
                } else {
                    record.put("status", "Absent");
                }
            } else {
                record.put("checkIn", null);
                record.put("checkOut", null);
                record.put("latitude", null);
                record.put("longitude", null);
                record.put("status", "Absent");
            }
            responseList.add(record);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/monthly-attendance")
    public ResponseEntity<List<Map<String, Object>>> getUserMonthlyAttendance(
            @PathVariable("userId") String userId,
            @RequestParam("month") String yearMonthStr
    ) {
        String[] parts = yearMonthStr.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        java.time.YearMonth yearMonth = java.time.YearMonth.of(year, month);
        java.time.LocalDate firstDay = yearMonth.atDay(1);
        java.time.LocalDate lastDay = yearMonth.atEndOfMonth();

        java.time.Instant start = firstDay.atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
        java.time.Instant end = lastDay.plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).toInstant().minusNanos(1);

        List<Attendance> attendances = attendanceRepository.findByEmployee(userId);

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Attendance attendance : attendances) {
            java.time.Instant checkIn = attendance.getCheckIn();
            if (checkIn != null && !checkIn.isBefore(start) && !checkIn.isAfter(end)) {
                Map<String, Object> record = new HashMap<>();
                java.time.LocalDate localDate = checkIn.atZone(java.time.ZoneOffset.UTC).toLocalDate();
                record.put("date", localDate.toString());
                record.put("checkIn", checkIn.toString());
                record.put("checkOut", attendance.getCheckOut() != null ? attendance.getCheckOut().toString() : null);

                if (attendance.getCheckOut() != null) {
                    record.put("status", "Present");
                } else {
                    record.put("status", "Missing Out");
                }
                responseList.add(record);
            }
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
