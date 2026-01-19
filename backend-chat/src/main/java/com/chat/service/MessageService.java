package com.chat.service;

import com.chat.model.Message;
import com.chat.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findByRoomId(Long roomId) {
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}