package com.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateRoomDTO {
    private String name;
    private String description;
    private boolean isPrivate;
}