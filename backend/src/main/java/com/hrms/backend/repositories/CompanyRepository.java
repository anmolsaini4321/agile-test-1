package com.hrms.backend.repositories;

import com.hrms.backend.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,String> {
    Optional<Company> findByCompanyCode(String companyCode);
    Optional<Company> findByHr(String userId);
}
