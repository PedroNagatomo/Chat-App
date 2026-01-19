package com.chat.service;

import com.chat.dto.ChatMessage;
import com.chat.model.ChatRoom;
import com.chat.model.Message;
import com.chat.model.User;
import com.chat.repository.ChatRoomRepository;
import com.chat.repository.MessageRepository;
import com.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            ChatRoom room = chatRoomRepository.findById(chatMessage.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

            Message message = new Message();
            message.setContent(chatMessage.getContent());

            // Converter MessageType
            if (chatMessage.getType() == ChatMessage.MessageType.JOIN) {
                message.setType(Message.MessageType.JOIN);
            } else if (chatMessage.getType() == ChatMessage.MessageType.LEAVE) {
                message.setType(Message.MessageType.LEAVE);
            } else if (chatMessage.getType() == ChatMessage.MessageType.SYSTEM) {
                message.setType(Message.MessageType.SYSTEM);
            } else {
                message.setType(Message.MessageType.CHAT);
            }

            message.setSender(user);
            message.setRoom(room);

            messageRepository.save(message);

            chatMessage.setSender(user.getUsername());
            chatMessage.setSenderColor(user.getAvatarColor());
            chatMessage.setTimestamp(message.getTimestamp());

            return chatMessage;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar mensagem: " + e.getMessage(), e);
        }
    }

    public List<ChatMessage> getMessageHistory(Long roomId) {
        try {
            List<Message> messages = messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
            List<ChatMessage> result = new ArrayList<>();

            for (Message message : messages) {
                result.add(convertToDTO(message));
            }

            return result;
        } catch (Exception e) {
            System.err.println("Erro ao buscar histórico: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ChatMessage> getLastMessages(Long roomId, int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<Message> messages = messageRepository.findLastMessages(roomId, pageable);
            List<ChatMessage> result = new ArrayList<>();

            for (Message message : messages) {
                result.add(convertToDTO(message));
            }

            return result;
        } catch (Exception e) {
            System.err.println("Erro ao buscar últimas mensagens: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<String> getUsersInRoom(Long roomId) {
        try {
            return chatRoomRepository.findById(roomId)
                    .map(room -> {
                        List<String> usernames = new ArrayList<>();
                        for (User user : room.getMembers()) {
                            usernames.add(user.getUsername());
                        }
                        return usernames;
                    })
                    .orElse(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuários na sala: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ChatRoom createRoom(String name, String description, boolean isPrivate) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setDescription(description);
        room.setPrivate(isPrivate);
        return chatRoomRepository.save(room);
    }

    private ChatMessage convertToDTO(Message message) {
        ChatMessage dto = new ChatMessage();

        // Converter MessageType de volta
        if (message.getType() == Message.MessageType.JOIN) {
            dto.setType(ChatMessage.MessageType.JOIN);
        } else if (message.getType() == Message.MessageType.LEAVE) {
            dto.setType(ChatMessage.MessageType.LEAVE);
        } else if (message.getType() == Message.MessageType.SYSTEM) {
            dto.setType(ChatMessage.MessageType.SYSTEM);
        } else {
            dto.setType(ChatMessage.MessageType.CHAT);
        }

        dto.setContent(message.getContent());
        dto.setSender(message.getSender().getUsername());
        dto.setSenderColor(message.getSender().getAvatarColor());
        dto.setRoomId(message.getRoom().getId());
        dto.setTimestamp(message.getTimestamp());

        return dto;
    }
}