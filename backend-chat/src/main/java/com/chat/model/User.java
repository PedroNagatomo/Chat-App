package com.chat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 20, message = "Username deve ter entre 3 e 20 caracteres")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(unique = true, nullable = false)
    private String email;

    private String avatarColor;

    @Column(name = "is_online")
    private boolean online = false;

    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "members")
    private Set<ChatRoom> rooms = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.avatarColor = generateRandomColor();
        this.lastSeen = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    private String generateRandomColor() {
        String[] colors = {"#FF6B6B", "#4ECDC4", "#FFD166", "#06D6A0", "#118AB2", "#EF476F",
                "#7209B7", "#F72585", "#3A0CA3", "#4361EE"};
        return colors[(int) (Math.random() * colors.length)];
    }
}