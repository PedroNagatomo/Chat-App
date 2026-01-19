package com.chat.controller;

import com.chat.dto.RoomDTO;
import com.chat.model.ChatRoom;
import com.chat.model.User;
import com.chat.repository.ChatRoomRepository;
import com.chat.repository.UserRepository;
import com.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms(){
        List<RoomDTO> rooms = chatRoomRepository.findByIsPrivateFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long roomId){
        return chatRoomRepository.findById(roomId)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO roomDTO){
        ChatRoom room = new ChatRoom();
        room.setName(roomDTO.getName());
        room.setDescription(roomDTO.getDescription);
        room.setPrivate(roomDTO.isPrivate());

        ChatRoom savedRoom = chatRoomRepository.save(room);
        return ResponseEntity.ok(convertToDTO(savedRoom));
    }

    @PostMapping("/{roomId}/join/{userId}")
    public ResponseEntity<?> joinRoom(@PathVariable Long roomId, @PathVariable Long userId){
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Sala não encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        room.getMembers().add(user);
        chatRoomRepository.save(room);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoomDTO>> getUserRooms(@PathVariable Long userId){
        List<RoomDTO> rooms = chatRoomRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    private RoomDTO convertToDTO(ChatRoom room){
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setDescription(room.getDescription());
        dto.setPrivate(room.isPrivate());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setMemberCount(room.getMembers() != null ? room.getMembers().size() : 0);
        return dto;
    }
}
