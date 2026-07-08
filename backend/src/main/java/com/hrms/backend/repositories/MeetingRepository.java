package com.hrms.backend.repositories;

import com.hrms.backend.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, String> {

    @Query("SELECT m FROM Meeting m JOIN m.participants p WHERE m.companyCode = :companyCode AND p = :participant")
    List<Meeting> findByCompanyCodeAndParticipantsContains(@Param("companyCode") String companyCode, @Param("participant") String participant);

    List<Meeting> findByCompanyCodeAndOrganizer(String companyCode, String organizer);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Meeting m JOIN m.participants p WHERE m.companyCode = :companyCode AND p = :participantId AND m.startTime <= :end AND m.endTime >= :start")
    boolean existsByCompanyCodeAndParticipantsContainsAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            @Param("companyCode") String companyCode, 
            @Param("participantId") String participantId, 
            @Param("end") LocalDateTime end, 
            @Param("start") LocalDateTime start
    );
}
