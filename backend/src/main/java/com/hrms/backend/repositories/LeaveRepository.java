package com.hrms.backend.repositories;

import com.hrms.backend.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest,String> {
    List<LeaveRequest> findAllByUserIdAndCompanyCode(String userId,String companyCode); // employee
    List<LeaveRequest> findAllByCompanyCode(String companyCode);
}
