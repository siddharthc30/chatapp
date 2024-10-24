package com.chatapp.service;

import com.chatapp.entity.ChatMessage;
import com.chatapp.entity.ChatConversation;
import com.chatapp.entity.User;
import com.chatapp.service.UserService;
import com.chatapp.repository.ChatMessageRepository;
import com.chatapp.repository.ChatConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final Logger LOG = LogManager.getLogger(ChatService.class);

    private final ChatMessageRepository messageRepository;
    private final ChatConversationRepository conversationRepository;
    private final UserService userService;
    private final TranslationService translationService;

    public String createOrGetConversationId(String user1, String user2) {
        String[] participants = {user1, user2};
        Arrays.sort(participants);
        String conversationId = String.format("%s_%s", participants[0], participants[1]);

        conversationRepository.findById(conversationId)
                .orElseGet(() -> {
                    ChatConversation conversation = new ChatConversation();
                    conversation.setId(conversationId);
                    conversation.setParticipant1(participants[0]);
                    conversation.setParticipant2(participants[1]);
                    conversation.setLastMessageTime(LocalDateTime.now());
                    return conversationRepository.save(conversation);
                });

        return conversationId;
    }

    @Transactional
    public ChatMessage saveMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());

        // Get language preferences
        User sender = userService.getUser(message.getSender());
        User receiver = userService.getUser(message.getReceiver());

        // Store original message
        message.setOriginalContent(message.getContent());
        message.setOriginalLanguage(sender.getPreferredLanguage());

        // Translate if languages are different
        if (!sender.getPreferredLanguage().equals(receiver.getPreferredLanguage())) {
            try {
                String translatedContent = translationService.translateText(
                        message.getContent(),
                        receiver.getPreferredLanguage()
                );
                message.setTranslatedContent(translatedContent);
                LOG.info("Message translated from {} to {}",
                        sender.getPreferredLanguage(),
                        receiver.getPreferredLanguage());
            } catch (Exception e) {
                LOG.error("Translation failed for message: {}", message.getContent(), e);
                // If translation fails, receiver will see original content
            }
        }

        // Update conversation last message time
        ChatConversation conversation = conversationRepository.findById(message.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        conversation.setLastMessageTime(message.getTimestamp());
        conversationRepository.save(conversation);

        return messageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory(String user1, String user2) {
        return messageRepository.findMessagesBetweenUsers(user1, user2);
    }

    public List<ChatConversation> getUserConversations(String username) {
        return conversationRepository.findByParticipant1OrParticipant2(username, username);
    }
}