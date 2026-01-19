package com.chat.controller;

import com.chat.ChatClientApplication;
import com.chat.model.User;
import com.chat.service.ApiService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    // ESTES SÃO os IDs que devem estar no FXML:
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label statusLabel;  // APENAS UM statusLabel!

    @FXML
    private void initialize() {
        System.out.println("=== LoginController.initialize() chamado ===");

        // Verifique quais componentes foram injetados
        System.out.println("usernameField: " + (usernameField != null ? "OK" : "NULL"));
        System.out.println("passwordField: " + (passwordField != null ? "OK" : "NULL"));
        System.out.println("loginButton: " + (loginButton != null ? "OK" : "NULL"));
        System.out.println("registerButton: " + (registerButton != null ? "OK" : "NULL"));
        System.out.println("statusLabel: " + (statusLabel != null ? "OK" : "NULL"));

        if (statusLabel != null) {
            statusLabel.setText("Digite usuário e senha");
        } else {
            System.err.println("ERRO CRÍTICO: statusLabel é null!");
            // Não tente usar statusLabel se for null
        }

        // Configurar ações dos botões
        if (loginButton != null) {
            loginButton.setOnAction(e -> handleLogin());
        }

        if (registerButton != null) {
            registerButton.setOnAction(e -> handleRegister());
        }
    }

    @FXML
    private void handleLogin() {
        System.out.println("=== handleLogin() chamado ===");

        String username = usernameField != null ? usernameField.getText() : "";
        String password = passwordField != null ? passwordField.getText() : "";

        System.out.println("Usuário: " + username);
        System.out.println("Senha: " + (password.isEmpty() ? "vazia" : "preenchida"));

        if (username.isEmpty() || password.isEmpty()) {
            if (statusLabel != null) {
                statusLabel.setText("Preencha todos os campos!");
            }
            return;
        }

        if (statusLabel != null) {
            statusLabel.setText("Conectando...");
        }

        // Teste: vá direto para a tela principal
        ChatClientApplication.showMainScreen();
    }

    @FXML
    private void handleRegister() {
        System.out.println("=== handleRegister() chamado ===");

        String username = usernameField != null ? usernameField.getText() : "";
        String password = passwordField != null ? passwordField.getText() : "";

        if (username.isEmpty() || password.isEmpty()) {
            if (statusLabel != null) {
                statusLabel.setText("Preencha todos os campos!");
            }
            return;
        }

        if (statusLabel != null) {
            statusLabel.setText("Registrando...");
        }

        // Aqui você pode adicionar lógica de registro real depois
        System.out.println("Registro solicitado para: " + username);
    }
}