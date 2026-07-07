package com.hrms.backend.repositories;

import com.hrms.backend.models.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository extends JpaRepository<Office,String> {
    Optional<Office> findByCompanyCode(String companyCode);
    Optional<Office> findByCreatedBy(String hrId);
}
