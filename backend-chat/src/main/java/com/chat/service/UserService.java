package com.chat.service;

import com.chat.dto.LoginResponse;
import com.chat.dto.UserDTO;
import com.chat.model.User;
import com.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // Armazenamento simples de sessões (em produção, use Redis)
    private final java.util.Map<String, Long> activeSessions = new java.util.concurrent.ConcurrentHashMap<>();

    public User registerUser(User user) {
        // Verificar se username já existe
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username já está em uso");
        }

        // Verificar se email já existe
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já está cadastrado");
        }

        // Em produção: usar BCryptPasswordEncoder
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public LoginResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return LoginResponse.error("Usuário não encontrado");
        }

        User user = userOptional.get();

        // Em produção: usar BCryptPasswordEncoder.matches()
        // if (!passwordEncoder.matches(password, user.getPassword())) {
        if (!user.getPassword().equals(password)) {
            return LoginResponse.error("Senha incorreta");
        }

        // Atualizar status
        user.setOnline(true);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);

        // Criar token de sessão
        String sessionToken = UUID.randomUUID().toString();
        activeSessions.put(sessionToken, user.getId());

        return LoginResponse.success(convertToDTO(user), sessionToken);
    }

    public void logout(String sessionToken) {
        Long userId = activeSessions.remove(sessionToken);
        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> {
                user.setOnline(false);
                user.setLastSeen(LocalDateTime.now());
                userRepository.save(user);
            });
        }
    }

    public boolean validateSession(String sessionToken) {
        return activeSessions.containsKey(sessionToken);
    }

    public Optional<UserDTO> getUserBySession(String sessionToken) {
        Long userId = activeSessions.get(sessionToken);
        if (userId != null) {
            return userRepository.findById(userId).map(this::convertToDTO);
        }
        return Optional.empty();
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    public List<UserDTO> getOnlineUsers() {
        return userRepository.findOnlineUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUserStatus(Long userId, boolean online) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setOnline(online);
        user.setLastSeen(LocalDateTime.now());

        return convertToDTO(userRepository.save(user));
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatarColor(user.getAvatarColor());
        dto.setOnline(user.isOnline());
        dto.setLastSeen(user.getLastSeen());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}