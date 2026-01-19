package com.chat.service;

import com.chat.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.function.Consumer;

public class WebSocketService {
    private WebSocket webSocket;
    private final ObjectMapper mapper;
    private Consumer<Message> messageHandler;
    private Consumer<String> statusHandler;

    public WebSocketService() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void connect(String username, Long roomId) {
        try {
            // Primeiro tentar descobrir o endpoint correto
            testWebSocketEndpoints(username, roomId);
        } catch (Exception e) {
            Platform.runLater(() -> {
                if (statusHandler != null) {
                    statusHandler.accept("WebSocket não disponível");
                }
            });
        }
    }

    private void testWebSocketEndpoints(String username, Long roomId) {
        String[] endpoints = {
                "ws://localhost:8080/chat-websocket",
                "ws://localhost:8080/ws",
                "ws://localhost:8080/websocket"
        };

        for (String endpoint : endpoints) {
            try {
                System.out.println("Testando WebSocket em: " + endpoint);

                HttpClient client = HttpClient.newHttpClient();
                webSocket = client.newWebSocketBuilder()
                        .buildAsync(URI.create(endpoint), new WebSocket.Listener() {
                            @Override
                            public void onOpen(WebSocket webSocket) {
                                System.out.println("WebSocket conectado em: " + endpoint);
                                Platform.runLater(() -> {
                                    if (statusHandler != null) {
                                        statusHandler.accept("WebSocket conectado!");
                                    }
                                });
                            }

                            @Override
                            public void onError(WebSocket webSocket, Throwable error) {
                                System.err.println("Erro WebSocket em " + endpoint + ": " + error.getMessage());
                            }
                        }).join();

                // Se chegou aqui sem exceção, provavelmente conectou
                return;

            } catch (Exception e) {
                System.err.println("Falha no endpoint " + endpoint + ": " + e.getMessage());
                continue;
            }
        }

        // Se nenhum endpoint funcionou
        Platform.runLater(() -> {
            if (statusHandler != null) {
                statusHandler.accept("WebSocket não encontrado");
            }
        });
    }

    public void sendMessage(Message message) {
        if (webSocket != null && webSocket.isOutputClosed()) {
            try {
                String json = mapper.writeValueAsString(message);
                webSocket.sendText(json, true);
            } catch (Exception e) {
                System.err.println("Erro ao enviar mensagem WebSocket: " + e.getMessage());
            }
        }
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Saindo");
        }
    }

    public void setMessageHandler(Consumer<Message> handler) {
        this.messageHandler = handler;
    }

    public void setStatusHandler(Consumer<String> handler) {
        this.statusHandler = handler;
    }
}