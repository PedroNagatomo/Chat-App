package com.chat.controller;

import com.chat.model.Message;
import com.chat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message savedMessage = messageService.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    // Buscar mensagens de uma sala
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<Message>> getRoomMessages(@PathVariable Long roomId) {
        List<Message> messages = messageService.findByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }

    // Buscar todas as mensagens (para debug)
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.findAll();
        return ResponseEntity.ok(messages);
    }
}
