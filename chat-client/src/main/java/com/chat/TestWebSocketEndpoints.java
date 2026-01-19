package com.chat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class TestWebSocketEndpoints {
    public static void main(String[] args) throws Exception {
        String[] endpoints = {
                "ws://localhost:8080/ws",
                "ws://localhost:8080/chat-websocket",
                "ws://localhost:8080/websocket",
                "ws://localhost:8080/ws/chat",
                "ws://localhost:8080/chat/ws",
                "ws://localhost:8080/stomp",
                "ws://localhost:8080/chat"
        };

        HttpClient client = HttpClient.newHttpClient();

        for (String endpoint : endpoints) {
            System.out.println("\n=== Testando: " + endpoint + " ===");

            try {
                CompletableFuture<WebSocket> wsFuture = client.newWebSocketBuilder()
                        .buildAsync(URI.create(endpoint), new WebSocket.Listener() {
                            @Override
                            public void onOpen(WebSocket webSocket) {
                                System.out.println("✓ CONECTADO!");
                                webSocket.sendClose(1000, "Teste concluído");
                            }

                            @Override
                            public CompletionStage<?> onText(WebSocket webSocket,
                                                             CharSequence data,
                                                             boolean last) {
                                System.out.println("Mensagem: " + data);
                                return WebSocket.Listener.super.onText(webSocket, data, last);
                            }

                            @Override
                            public void onError(WebSocket webSocket, Throwable error) {
                                System.out.println("✗ Erro: " + error.getMessage());
                            }
                        });

                // Aguarde um pouco para a conexão
                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println("✗ Exception: " + e.getMessage());
            }
        }

        // Mantenha o programa rodando
        Thread.sleep(5000);
    }
}