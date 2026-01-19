package com.chat.controller;

import com.chat.dto.ChatMessage;
import com.chat.service.ChatService;
import com.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Payload ChatMessage chatMessage,
            Principal principal) {

        log.info("Mensagem recebida de {} no room {}: {}",
                principal.getName(), chatMessage.getRoomId(), chatMessage.getContent());

        // Validar sessão
        if (!userService.validateSession(principal.getName())) {
            log.warn("Sessão inválida para usuário: {}", principal.getName());
            return;
        }

        // Salvar mensagem no banco
        ChatMessage savedMessage = chatService.saveMessage(chatMessage, principal.getName());

        // Enviar para todos inscritos na sala
        messagingTemplate.convertAndSend(
                "/topic/room/" + chatMessage.getRoomId(),
                savedMessage
        );
    }

    @MessageMapping("/chat.joinRoom/{roomId}")
    public void joinRoom(
            @DestinationVariable Long roomId,
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor,
            Principal principal) {

        log.info("Usuário {} entrando na sala {}", principal.getName(), roomId);

        // Adicionar usuário na sessão WebSocket
        headerAccessor.getSessionAttributes().put("username", principal.getName());
        headerAccessor.getSessionAttributes().put("roomId", roomId);

        // Notificar entrada do usuário
        ChatMessage joinMessage = new ChatMessage(
                ChatMessage.MessageType.JOIN,
                chatMessage.getSender() + " entrou no chat!",
                roomId
        );
        joinMessage.setSender(principal.getName());

        // Salvar mensagem de sistema
        chatService.saveMessage(joinMessage, principal.getName());

        // Enviar notificação para a sala
        messagingTemplate.convertAndSend("/topic/room/" + roomId, joinMessage);

        // Atualizar lista de usuários online na sala
        updateOnlineUsersInRoom(roomId);
    }

    @MessageMapping("/chat.leaveRoom/{roomId}")
    public void leaveRoom(
            @DestinationVariable Long roomId,
            Principal principal) {

        log.info("Usuário {} saindo da sala {}", principal.getName(), roomId);

        // Notificar saída do usuário
        ChatMessage leaveMessage = new ChatMessage(
                ChatMessage.MessageType.LEAVE,
                principal.getName() + " saiu do chat!",
                roomId
        );

        chatService.saveMessage(leaveMessage, principal.getName());
        messagingTemplate.convertAndSend("/topic/room/" + roomId, leaveMessage);
    }

    @MessageMapping("/chat.typing/{roomId}")
    public void typing(
            @DestinationVariable Long roomId,
            @Payload Map<String, String> payload,
            Principal principal) {

        Map<String, Object> typingMessage = Map.of(
                "username", principal.getName(),
                "isTyping", Boolean.parseBoolean(payload.get("isTyping")),
                "roomId", roomId
        );

        messagingTemplate.convertAndSend("/topic/typing/" + roomId, typingMessage);
    }

    private void updateOnlineUsersInRoom(Long roomId) {
        // Implementação simplificada - em produção, manteria uma lista em memória
        // ou consultaria o banco de dados
        log.debug("Atualizando usuários na sala {}", roomId);
    }

    // Endpoints REST para histórico
    @GetMapping("/messages/{roomId}")
    public List<ChatMessage> getMessageHistory(@PathVariable Long roomId) {
        return chatService.getMessageHistory(roomId);
    }

    @GetMapping("/rooms/{roomId}/last-messages")
    public List<ChatMessage> getLastMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "50") int limit) {
        return chatService.getLastMessages(roomId, limit);
    }

    @GetMapping("/rooms/{roomId}/users")
    public List<String> getRoomUsers(@PathVariable Long roomId) {
        return chatService.getUsersInRoom(roomId);
    }
}