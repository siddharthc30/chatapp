package com.chatapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_conversations")
@Data
public class ChatConversation {
    @Id
    private String id;

    private String participant1;
    private String participant2;
    private LocalDateTime lastMessageTime;
}