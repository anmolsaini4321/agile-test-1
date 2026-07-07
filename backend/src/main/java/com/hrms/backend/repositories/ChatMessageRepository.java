package com.hrms.backend.repositories;

import com.hrms.backend.models.ChatMessage;
import com.hrms.backend.models.enums.MessageStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.companyCode = :companyCode AND " +
           "((cm.sender = :userId AND cm.receiver = :otherUserId) OR " +
           "(cm.sender = :otherUserId AND cm.receiver = :userId))")
    List<ChatMessage> findChatHistoryBetweenUsers(
            @Param("companyCode") String companyCode, 
            @Param("userId") String userId, 
            @Param("otherUserId") String otherUserId, 
            Sort sort
    );

    List<ChatMessage> findByChatIdAndMessageStatus(String chatId, MessageStatus messageStatus);
}
