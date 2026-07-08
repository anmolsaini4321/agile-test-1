package com.hrms.backend.repositories;

import com.hrms.backend.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    
    @Query("SELECT c FROM Chat c WHERE (c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)")
    Optional<Chat> findByUser1AndUser2(@Param("user1") String user1, @Param("user2") String user2);

    @Query("SELECT c FROM Chat c WHERE c.companyCode = :companyCode AND (c.user1 = :userId OR c.user2 = :userId)")
    List<Chat> findChatsForUser(@Param("companyCode") String companyCode, @Param("userId") String userId);
}
