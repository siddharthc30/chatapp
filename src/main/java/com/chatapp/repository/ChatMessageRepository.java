package com.chatapp.repository;

import com.chatapp.entity.ChatMessage;
import com.chatapp.entity.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversationIdOrderByTimestampAsc(String conversationId);

    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender = ?1 AND m.receiver = ?2) OR " +
            "(m.sender = ?2 AND m.receiver = ?1) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findMessagesBetweenUsers(String user1, String user2);
}

