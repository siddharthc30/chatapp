package com.chatapp.repository;

import com.chatapp.entity.ChatMessage;
import com.chatapp.entity.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, String> {
    List<ChatConversation> findByParticipant1OrParticipant2(String participant1, String participant2);
}
