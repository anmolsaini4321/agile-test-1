package com.hrms.backend.models;

import com.hrms.backend.models.enums.MessageStatus;
import com.hrms.backend.models.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user1_id")
    private String user1;

    @Column(name = "user2_id")
    private String user2;

    private String lastMessage;
    private LocalDateTime lastUpdated;

    private String companyCode;

    @Enumerated(EnumType.STRING)
    private MessageType lastMessageType;

    @Enumerated(EnumType.STRING)
    private MessageStatus lastMessageStatus;

    @Column(name = "last_message_sender_id")
    private String lastMessageSender; // senderId
    private String lastSeenMessageId;
}
