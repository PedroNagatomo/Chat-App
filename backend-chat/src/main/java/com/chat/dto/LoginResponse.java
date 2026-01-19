package com.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private boolean success;
    private String message;
    private UserDTO user;
    private String sessionToken;

    public static LoginResponse success(UserDTO user, String token){
        return new LoginResponse(true, "Login bem-sucedido", user, token);
    }

    public static LoginResponse error(String message){
        return new LoginResponse(false, message, null, null);
    }
}
