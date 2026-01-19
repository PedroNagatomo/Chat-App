package com.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String avatarColor;
    private boolean online;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
}