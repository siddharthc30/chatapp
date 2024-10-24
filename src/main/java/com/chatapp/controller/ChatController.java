package com.chatapp.controller;

import com.chatapp.entity.ChatMessage;
import com.chatapp.entity.ChatConversation;
import com.chatapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // Generate or get conversation ID
        String conversationId = chatService.createOrGetConversationId(
                chatMessage.getSender(),
                chatMessage.getReceiver()
        );
        chatMessage.setConversationId(conversationId);

        // Save the message
        ChatMessage savedMessage = chatService.saveMessage(chatMessage);

        // Send to receiver
        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver(),
                "/queue/messages",
                savedMessage
        );

        // Send back to sender
        messagingTemplate.convertAndSendToUser(
                chatMessage.getSender(),
                "/queue/messages",
                savedMessage
        );
    }

    @GetMapping("/api/chat/history")
    public List<ChatMessage> getChatHistory(
            @RequestParam String user1,
            @RequestParam String user2) {
        return chatService.getChatHistory(user1, user2);
    }

    @GetMapping("/api/chat/conversations/{username}")
    public List<ChatConversation> getUserConversations(
            @PathVariable String username) {
        return chatService.getUserConversations(username);
    }
}