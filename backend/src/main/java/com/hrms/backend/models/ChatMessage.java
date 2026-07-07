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
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String chatId;

    @Column(name = "sender_id")
    private String sender;

    @Column(name = "receiver_id")
    private String receiver;

    private String content; // Encrypted message or file link

    @Enumerated(EnumType.STRING)
    private MessageType messageType; // TEXT, IMAGE, VIDEO

    private String companyCode;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;
}
