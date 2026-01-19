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
public class TestDataInitializer {

    @Bean
    public CommandLineRunner initTestData(
            UserRepository userRepository,
            ChatRoomRepository chatRoomRepository) {
        return args -> {
            // Criar usuários de teste
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setEmail("admin@chat.com");
                admin.setOnline(true);

                User joao = new User();
                joao.setUsername("joao");
                joao.setPassword("joao123");
                joao.setEmail("joao@email.com");
                joao.setOnline(true);

                User maria = new User();
                maria.setUsername("maria");
                maria.setPassword("maria123");
                maria.setEmail("maria@email.com");
                maria.setOnline(false);

                userRepository.save(admin);
                userRepository.save(joao);
                userRepository.save(maria);

                // Criar salas de chat
                ChatRoom geral = new ChatRoom();
                geral.setName("Geral");
                geral.setDescription("Chat geral para todos os usuários");
                geral.setPrivate(false);

                ChatRoom javaDevs = new ChatRoom();
                javaDevs.setName("Java Developers");
                javaDevs.setDescription("Discussões sobre Java e Spring");
                javaDevs.setPrivate(false);

                ChatRoom projeto = new ChatRoom();
                projeto.setName("Projeto Chat");
                projeto.setDescription("Desenvolvimento deste sistema de chat");
                projeto.setPrivate(false);

                chatRoomRepository.save(geral);
                chatRoomRepository.save(javaDevs);
                chatRoomRepository.save(projeto);

                System.out.println("Dados de teste criados!");
                System.out.println("Usuários: admin/admin123, joao/joao123, maria/maria123");
            }
        };
    }
}