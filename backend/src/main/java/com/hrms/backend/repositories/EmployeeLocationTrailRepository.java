package com.hrms.backend.repositories;

import com.hrms.backend.models.EmployeeLocationTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeLocationTrailRepository extends JpaRepository<EmployeeLocationTrail, String> {
    List<EmployeeLocationTrail> findByAttendanceIdOrderByRecordedAtAsc(String attendanceId);
    List<EmployeeLocationTrail> findByEmployeeIdOrderByRecordedAtAsc(String employeeId);
}
