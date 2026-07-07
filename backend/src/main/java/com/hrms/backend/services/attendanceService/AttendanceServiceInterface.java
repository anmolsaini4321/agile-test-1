package com.hrms.backend.services.attendanceService;

import com.hrms.backend.dtos.entityDtos.Attendance.AttendanceRequestDto;
import com.hrms.backend.dtos.entityDtos.Attendance.AttendanceResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceServiceInterface  {

    AttendanceResponseDto markEmployeeAttendance(
            String userId,
            AttendanceRequestDto attendanceRequestDto
    );

    List<AttendanceResponseDto> getEmployeeAttendanceHistory(String userId);
    List<AttendanceResponseDto> getCompanyAttendanceByDate(String hrId, Optional<LocalDate> optionalDate);
    
    void recordLocationTrail(String userId, String attendanceId, Double latitude, Double longitude);
    List<com.hrms.backend.models.EmployeeLocationTrail> getLocationTrailByEmployeeAndDate(String employeeId, LocalDate date);
}
