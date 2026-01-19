package com.chat.config;

import com.chat.model.ChatRoom;
import com.chat.model.User;
import com.chat.repository.ChatRoomRepository;
import com.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            ChatRoomRepository chatRoomRepository) {
        return args -> {
            // Verificar se já existem dados
            if (userRepository.count() == 0) {
                System.out.println("Inicializando dados...");

                // Criar usuários
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123"); // Em produção, use BCrypt
                admin.setEmail("admin@chat.com");
                admin.setOnline(true);
                admin.setLastSeen(LocalDateTime.now());

                User joao = new User();
                joao.setUsername("joao");
                joao.setPassword("joao123");
                joao.setEmail("joao@email.com");
                joao.setOnline(true);
                joao.setLastSeen(LocalDateTime.now());

                User maria = new User();
                maria.setUsername("maria");
                maria.setPassword("maria123");
                maria.setEmail("maria@email.com");
                maria.setOnline(false);
                maria.setLastSeen(LocalDateTime.now());

                userRepository.save(admin);
                userRepository.save(joao);
                userRepository.save(maria);

                // Criar salas
                ChatRoom geral = new ChatRoom();
                geral.setName("Geral");
                geral.setDescription("Chat geral para todos os usuários");
                geral.setPrivate(false);
                geral.getMembers().add(admin);
                geral.getMembers().add(joao);
                geral.getMembers().add(maria);

                ChatRoom javaDevs = new ChatRoom();
                javaDevs.setName("Java Developers");
                javaDevs.setDescription("Discussões sobre Java e Spring");
                javaDevs.setPrivate(false);
                javaDevs.getMembers().add(admin);
                javaDevs.getMembers().add(joao);

                ChatRoom projetoChat = new ChatRoom();
                projetoChat.setName("Projeto Chat");
                projetoChat.setDescription("Discussões sobre o desenvolvimento deste chat");
                projetoChat.setPrivate(false);
                projetoChat.getMembers().add(admin);
                projetoChat.getMembers().add(maria);

                chatRoomRepository.save(geral);
                chatRoomRepository.save(javaDevs);
                chatRoomRepository.save(projetoChat);

                System.out.println("Dados inicializados com sucesso!");
            }
        };
    }
}