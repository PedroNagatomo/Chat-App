package com.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RoomDTO {

    public String getDescription;
    private Long id;
    private String name;
    private String description;
    private boolean isPrivate;
    private LocalDateTime createdAt;
    private int memberCount;
}
