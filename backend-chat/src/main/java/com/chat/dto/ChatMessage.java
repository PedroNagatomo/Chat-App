package com.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String senderColor;
    private Long roomId;
    private LocalDateTime timestamp;

    public enum MessageType {
        CHAT, JOIN, LEAVE, SYSTEM
    }

    // Construtor para mensagens de sistema
    public ChatMessage(MessageType type, String content, Long roomId) {
        this.type = type;
        this.content = content;
        this.roomId = roomId;
        this.timestamp = LocalDateTime.now();
    }

    // Construtor para mensagens de chat normais
    public ChatMessage(String content, String sender, String senderColor, Long roomId) {
        this.type = MessageType.CHAT;
        this.content = content;
        this.sender = sender;
        this.senderColor = senderColor;
        this.roomId = roomId;
        this.timestamp = LocalDateTime.now();
    }
}