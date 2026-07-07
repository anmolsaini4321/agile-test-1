package com.hrms.backend.repositories;

import com.hrms.backend.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,String> {
    List<Task> findAllByCompanyCode(String companyCode);

    @Query("SELECT t FROM Task t JOIN t.employees e WHERE t.companyCode = :companyCode AND e = :userId")
    List<Task> findAllByCompanyCodeAndEmployeesContaining(@Param("companyCode") String companyCode, @Param("userId") String userId);
}
