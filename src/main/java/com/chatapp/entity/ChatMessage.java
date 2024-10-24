package com.chatapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conversationId;
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp;

    private String originalContent;
    private String translatedContent;
    private String originalLanguage;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public String getContent() {
       if(translatedContent == null){
           return content;
       }
       else{
           return translatedContent;
       }
        //return content;
    }

}


