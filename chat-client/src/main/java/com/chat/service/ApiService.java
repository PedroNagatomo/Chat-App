package com.chat.service;

import com.chat.model.Message;
import com.chat.model.User;
import com.chat.model.ChatRoom;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ApiService {
    public static final String BASE_URL = "http://localhost:8080/api"; // Mude para public
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static String currentToken;
    private static String currentUsername;

    public static void setCurrentUser(String username, String token) {
        currentUsername = username;
        currentToken = token;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentToken() {
        return currentToken;
    }

    // Login
    public static CompletableFuture<Boolean> loginUser(String username, String password) {
        try {
            String json = String.format(
                    "{\"username\":\"%s\",\"password\":\"%s\"}",
                    username, password
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        System.out.println("Login response: " + response.statusCode() + " - " + response.body());
                        if (response.statusCode() == 200) {
                            try {
                                // O backend retorna um JSON com token ou informações do usuário
                                String responseBody = response.body();
                                setCurrentUser(username, responseBody); // Guarda a resposta completa como "token"
                                return true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                        return false;
                    })
                    .exceptionally(e -> {
                        System.err.println("Erro no login: " + e.getMessage());
                        return false;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }

    // Registro
    public static CompletableFuture<Boolean> registerUser(String username, String email, String password) {
        try {
            String json = String.format(
                    "{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                    username, email, password
            );

            System.out.println("Enviando registro: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/users/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        System.out.println("Resposta registro: " + response.statusCode() + " - " + response.body());
                        return response.statusCode() == 200 || response.statusCode() == 201;
                    })
                    .exceptionally(e -> {
                        System.err.println("Erro no registro: " + e.getMessage());
                        return false;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }

    // Obter todas as salas de chat
    public static CompletableFuture<List<ChatRoom>> getChatRooms() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/rooms"))
                .header("Authorization", "Bearer " + currentToken)
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        System.out.println("Resposta salas: " + response.statusCode() + " - " + response.body());
                        if (response.statusCode() == 200) {
                            return mapper.readValue(response.body(),
                                    new TypeReference<List<ChatRoom>>() {});
                        } else {
                            System.out.println("Erro ao buscar salas: " + response.body());
                            return List.<ChatRoom>of();
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao parsear salas: " + e.getMessage());
                        e.printStackTrace();
                        return List.<ChatRoom>of();
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Erro na requisição de salas: " + e.getMessage());
                    return List.<ChatRoom>of();
                });
    }

    // Criar nova sala
    public static CompletableFuture<Boolean> createChatRoom(String name, String description) {
        try {
            String json = String.format(
                    "{\"name\":\"%s\",\"description\":\"%s\"}",
                    name, description
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/rooms"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + currentToken)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        System.out.println("Resposta criar sala: " + response.statusCode() + " - " + response.body());
                        return response.statusCode() == 200 || response.statusCode() == 201;
                    })
                    .exceptionally(e -> {
                        System.err.println("Erro ao criar sala: " + e.getMessage());
                        return false;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }

    // Obter usuários online
    public static CompletableFuture<List<User>> getOnlineUsers() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/online"))
                .header("Authorization", "Bearer " + currentToken)
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        System.out.println("Resposta usuários online: " + response.statusCode() + " - " + response.body());
                        if (response.statusCode() == 200) {
                            return mapper.readValue(response.body(),
                                    new TypeReference<List<User>>() {});
                        } else {
                            return List.<User>of();
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao parsear usuários online: " + e.getMessage());
                        e.printStackTrace();
                        return List.<User>of();
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Erro na requisição de usuários online: " + e.getMessage());
                    return List.<User>of();
                });
    }

    public static CompletableFuture<List<Message>> getRoomMessages(Long roomId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/rooms/" + roomId + "/messages"))
                    .header("Authorization", "Bearer " + currentToken)
                    .GET()
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            if (response.statusCode() == 200) {
                                return mapper.readValue(response.body(),
                                        new TypeReference<List<Message>>() {});
                            } else {
                                System.out.println("Erro ao buscar mensagens: " + response.statusCode());
                                return List.<Message>of();
                            }
                        } catch (Exception e) {
                            System.err.println("Erro ao parsear mensagens: " + e.getMessage());
                            return List.<Message>of();
                        }
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(List.of());
        }
    }

    // Enviar mensagem via HTTP (fallback quando WebSocket não funciona)
    public static CompletableFuture<Boolean> sendMessageViaHttp(Message message) {
        try {
            String json = mapper.writeValueAsString(message);

            System.out.println("Enviando mensagem via HTTP: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/messages"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + currentToken)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        System.out.println("Resposta envio mensagem: " + response.statusCode() + " - " + response.body());
                        return response.statusCode() == 200 || response.statusCode() == 201;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }

    // Verificar se existe endpoint de mensagens
    public static CompletableFuture<Boolean> checkMessagesEndpoint() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/messages"))
                    .header("Authorization", "Bearer " + currentToken)
                    .GET()
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        System.out.println("Check messages endpoint: " + response.statusCode());
                        return response.statusCode() != 404;
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    public static void testEndpoints() {
        String[] endpoints = {
                "/api/messages",
                "/api/rooms/1/messages",
                "/api/chat",
                "/api/chat/messages"
        };

        for (String endpoint : endpoints) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080" + endpoint))
                        .header("Authorization", "Bearer " + currentToken)
                        .GET()
                        .build();

                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            System.out.println("Endpoint " + endpoint + ": " + response.statusCode());
                            if (response.statusCode() != 404) {
                                System.out.println("Resposta: " + response.body().substring(0, Math.min(100, response.body().length())));
                            }
                        });
            } catch (Exception e) {
                System.err.println("Erro testando " + endpoint + ": " + e.getMessage());
            }
        }
    }

    // Vamos usar um approach diferente: armazenar mensagens nas salas como descrição
// (Isso é temporário até termos endpoints de mensagens)

    public static CompletableFuture<Boolean> sendMessageAsRoomUpdate(Message message) {
        try {
            // Primeiro, pegue a sala atual
            HttpRequest getRoomRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/rooms/" + message.getRoomId()))
                    .header("Authorization", "Bearer " + currentToken)
                    .GET()
                    .build();

            return client.sendAsync(getRoomRequest, HttpResponse.BodyHandlers.ofString())
                    .thenCompose(roomResponse -> {
                        if (roomResponse.statusCode() == 200) {
                            try {
                                // Parse a sala existente
                                ChatRoom room = mapper.readValue(roomResponse.body(), ChatRoom.class);

                                // Adicione a mensagem à descrição (formato temporário)
                                String newDescription = room.getDescription() != null ?
                                        room.getDescription() + "\n" + message.getSender() + ": " + message.getContent() :
                                        message.getSender() + ": " + message.getContent();

                                // Atualize a sala com a nova "mensagem" na descrição
                                String updateJson = String.format(
                                        "{\"name\":\"%s\",\"description\":\"%s\"}",
                                        room.getName(), newDescription
                                );

                                HttpRequest updateRequest = HttpRequest.newBuilder()
                                        .uri(URI.create(BASE_URL + "/rooms/" + message.getRoomId()))
                                        .header("Content-Type", "application/json")
                                        .header("Authorization", "Bearer " + currentToken)
                                        .PUT(HttpRequest.BodyPublishers.ofString(updateJson))
                                        .build();

                                return client.sendAsync(updateRequest, HttpResponse.BodyHandlers.ofString())
                                        .thenApply(response -> {
                                            System.out.println("Atualização de sala: " + response.statusCode());
                                            return response.statusCode() == 200;
                                        });
                            } catch (Exception e) {
                                return CompletableFuture.completedFuture(false);
                            }
                        }
                        return CompletableFuture.completedFuture(false);
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    // Buscar "mensagens" da descrição da sala
    public static CompletableFuture<List<String>> getMessagesFromRoom(Long roomId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/rooms/" + roomId))
                .header("Authorization", "Bearer " + currentToken)
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        if (response.statusCode() == 200) {
                            ChatRoom room = mapper.readValue(response.body(), ChatRoom.class);
                            if (room.getDescription() != null && !room.getDescription().isEmpty()) {
                                // Dividir a descrição em linhas (mensagens)
                                return Arrays.asList(room.getDescription().split("\n"));
                            }
                        }
                        return List.<String>of();
                    } catch (Exception e) {
                        return List.<String>of();
                    }
                });
    }
}