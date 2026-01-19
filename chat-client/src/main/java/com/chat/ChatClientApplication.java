package com.chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ChatClientApplication extends Application {

    private static Stage primaryStage;
    private static String currentUser;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginScreen();
        stage.setTitle("Chat App");
        stage.show();
    }

    public static void showLoginScreen() {
        // Crie a interface de login programaticamente
        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setBackground(new Background(new BackgroundFill(
                Color.web("#667eea"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Card central
        VBox loginCard = new VBox(20);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(15), Insets.EMPTY)));
        loginCard.setPadding(new Insets(40));
        loginCard.setMaxWidth(400);
        loginCard.setMaxHeight(500);
        loginCard.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 25, 0, 0, 0);");

        // Título
        Label title = new Label("💬 Chat App");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#333333"));

        Label subtitle = new Label("Conecte-se com amigos");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#666666"));

        // Formulário
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER_LEFT);

        // Campo usuário
        Label userLabel = new Label("Usuário");
        userLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        userLabel.setTextFill(Color.web("#333333"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Digite seu nome de usuário");
        usernameField.setPrefHeight(40);
        usernameField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Campo senha
        Label passLabel = new Label("Senha");
        passLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        passLabel.setTextFill(Color.web("#333333"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Digite sua senha");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Status
        Label statusLabel = new Label("Pronto para conectar");
        statusLabel.setFont(Font.font("Segoe UI", 12));
        statusLabel.setTextFill(Color.web("#666666"));

        // Botões
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Entrar");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; " +
                "-fx-background-radius: 5; -fx-pref-width: 120; -fx-pref-height: 40;");

        Button registerButton = new Button("Registrar");
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; " +
                "-fx-background-radius: 5; -fx-pref-width: 120; -fx-pref-height: 40;");

        buttons.getChildren().addAll(loginButton, registerButton);

        // Rodapé
        Label footer = new Label("© 2024 Chat App - Desenvolvido com JavaFX & Spring Boot");
        footer.setFont(Font.font("Segoe UI", 11));
        footer.setTextFill(Color.web("#999999"));

        // Ações dos botões
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Preencha todos os campos!");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            statusLabel.setText("Conectando...");
            statusLabel.setTextFill(Color.web("#666666"));
            loginButton.setDisable(true);
            registerButton.setDisable(true);

            // Simulação de login
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Simula delay de rede
                    Platform.runLater(() -> {
                        currentUser = username;
                        showMainScreen();
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Preencha todos os campos!");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            if (password.length() < 3) {
                statusLabel.setText("Senha muito curta!");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            statusLabel.setText("Registrando...");
            statusLabel.setTextFill(Color.web("#666666"));

            // Simulação de registro
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        statusLabel.setText("Usuário registrado! Faça login.");
                        statusLabel.setTextFill(Color.GREEN);
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // Montar o formulário
        form.getChildren().addAll(userLabel, usernameField, passLabel, passwordField);

        // Montar o card
        loginCard.getChildren().addAll(title, subtitle, form, statusLabel, buttons, footer);

        // Centralizar
        StackPane centerPane = new StackPane(loginCard);
        mainContainer.getChildren().add(centerPane);

        Scene scene = new Scene(mainContainer, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showMainScreen() {
        // Tela principal com salas
        BorderPane mainPane = new BorderPane();
        mainPane.setBackground(new Background(new BackgroundFill(
                Color.web("#f8f9fa"), CornerRadii.EMPTY, Insets.EMPTY)));

        // TOPO - Header
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setBackground(new Background(new BackgroundFill(
                Color.web("#667eea"), CornerRadii.EMPTY, Insets.EMPTY)));

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button logoutButton = new Button("← Sair");
        logoutButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); " +
                "-fx-text-fill: white; -fx-background-radius: 5;");

        Label welcomeLabel = new Label("Bem-vindo, " + (currentUser != null ? currentUser : "Usuário") + "!");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        welcomeLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button refreshButton = new Button("Atualizar");
        refreshButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); " +
                "-fx-text-fill: white; -fx-background-radius: 5;");

        topBar.getChildren().addAll(logoutButton, welcomeLabel, spacer, refreshButton);

        Label subtitle = new Label("Salas de Chat Disponíveis");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#E3F2FD"));

        header.getChildren().addAll(topBar, subtitle);
        mainPane.setTop(header);

        // CENTRO - SplitPane com salas e usuários
        SplitPane centerSplit = new SplitPane();
        centerSplit.setDividerPositions(0.7);

        // Painel Esquerdo - Salas
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(20));

        // Card de criar sala
        VBox createRoomCard = new VBox(10);
        createRoomCard.setPadding(new Insets(15));
        createRoomCard.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        createRoomCard.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        Label createRoomLabel = new Label("Criar Nova Sala");
        createRoomLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        createRoomLabel.setTextFill(Color.web("#333333"));

        HBox roomInputs = new HBox(10);
        roomInputs.setAlignment(Pos.CENTER_LEFT);

        TextField roomNameField = new TextField();
        roomNameField.setPromptText("Nome da sala");
        roomNameField.setPrefHeight(40);
        HBox.setHgrow(roomNameField, Priority.ALWAYS);

        TextField roomDescField = new TextField();
        roomDescField.setPromptText("Descrição");
        roomDescField.setPrefHeight(40);
        HBox.setHgrow(roomDescField, Priority.ALWAYS);

        Button createButton = new Button("Criar");
        createButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-pref-height: 40; -fx-pref-width: 80;");

        roomInputs.getChildren().addAll(roomNameField, roomDescField, createButton);
        createRoomCard.getChildren().addAll(createRoomLabel, roomInputs);

        // Tabela de salas
        Label roomsLabel = new Label("Salas Disponíveis");
        roomsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        roomsLabel.setTextFill(Color.web("#333333"));

        TableView<String[]> roomsTable = new TableView<>();
        roomsTable.setPlaceholder(new Label("Nenhuma sala disponível"));
        roomsTable.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        // Colunas da tabela
        TableColumn<String[], String> nameCol = new TableColumn<>("Sala");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        nameCol.setPrefWidth(150);

        TableColumn<String[], String> descCol = new TableColumn<>("Descrição");
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        descCol.setPrefWidth(250);

        TableColumn<String[], String> usersCol = new TableColumn<>("Usuários");
        usersCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
        usersCol.setPrefWidth(80);

        roomsTable.getColumns().addAll(nameCol, descCol, usersCol);

        // Dados de exemplo
        roomsTable.getItems().addAll(
                new String[]{"Geral", "Conversas gerais", "5"},
                new String[]{"Desenvolvimento", "Discussões técnicas", "3"},
                new String[]{"Social", "Conversas informais", "7"}
        );

        // Botão entrar na sala
        Button joinButton = new Button("Entrar na Sala Selecionada");
        joinButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-pref-height: 45;");
        joinButton.setDisable(true);

        // Quando selecionar uma sala, habilitar botão
        roomsTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newSelection) -> {
            joinButton.setDisable(newSelection == null);
        });

        joinButton.setOnAction(e -> {
            String[] selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
            if (selectedRoom != null) {
                System.out.println("Entrando na sala: " + selectedRoom[0]);
                showChatScreen(selectedRoom[0]);
            }
        });

        createButton.setOnAction(e -> {
            String name = roomNameField.getText().trim();
            String desc = roomDescField.getText().trim();

            if (!name.isEmpty()) {
                roomsTable.getItems().add(new String[]{name, desc, "1"});
                roomNameField.clear();
                roomDescField.clear();
            }
        });

        leftPanel.getChildren().addAll(createRoomCard, roomsLabel, roomsTable, joinButton);

        // Painel Direito - Usuários Online
        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(20, 20, 20, 0));

        Label usersLabel = new Label("👥 Usuários Online");
        usersLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        usersLabel.setTextFill(Color.web("#333333"));

        TableView<String[]> usersTable = new TableView<>();
        usersTable.setPlaceholder(new Label("Nenhum usuário online"));
        usersTable.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        TableColumn<String[], String> userCol = new TableColumn<>("Usuário");
        userCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        userCol.setPrefWidth(150);

        TableColumn<String[], String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        statusCol.setPrefWidth(100);

        usersTable.getColumns().addAll(userCol, statusCol);

        // Dados de exemplo
        usersTable.getItems().addAll(
                new String[]{"Ana", "Online"},
                new String[]{"Carlos", "Online"},
                new String[]{"Maria", "Offline"},
                new String[]{"João", "Online"}
        );

        // Card de dicas
        VBox tipsCard = new VBox(10);
        tipsCard.setPadding(new Insets(15));
        tipsCard.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        tipsCard.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        Label tipsLabel = new Label("💡 Dicas Rápidas");
        tipsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        tipsLabel.setTextFill(Color.web("#333333"));

        VBox tipsList = new VBox(5);
        tipsList.getChildren().addAll(
                new Label("• Clique duas vezes em uma sala para entrar"),
                new Label("• Atualize para ver novos usuários"),
                new Label("• Crie salas para diferentes tópicos")
        );

        for (var node : tipsList.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setFont(Font.font("Segoe UI", 12));
                ((Label) node).setTextFill(Color.web("#666666"));
            }
        }

        tipsCard.getChildren().addAll(tipsLabel, tipsList);
        rightPanel.getChildren().addAll(usersLabel, usersTable, tipsCard);

        // Ações dos botões
        logoutButton.setOnAction(e -> showLoginScreen());
        refreshButton.setOnAction(e -> {
            // Simula atualização
            System.out.println("Atualizando...");
        });

        centerSplit.getItems().addAll(leftPanel, rightPanel);
        mainPane.setCenter(centerSplit);

        Scene scene = new Scene(mainPane, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showChatScreen(String roomName) {
        // Tela de chat
        BorderPane chatPane = new BorderPane();
        chatPane.setBackground(new Background(new BackgroundFill(
                Color.web("#f5f7fa"), CornerRadii.EMPTY, Insets.EMPTY)));

        // TOPO - Header do chat
        VBox chatHeader = new VBox(5);
        chatHeader.setPadding(new Insets(15, 20, 15, 20));
        chatHeader.setBackground(new Background(new BackgroundFill(
                Color.web("#667eea"), CornerRadii.EMPTY, Insets.EMPTY)));

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Voltar");
        backButton.setStyle("-fx-background-color: rgba(255,255,255,0.2); " +
                "-fx-text-fill: white; -fx-background-radius: 20;");

        VBox roomInfo = new VBox(3);
        Label roomLabel = new Label("💬 " + roomName);
        roomLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        roomLabel.setTextFill(Color.WHITE);

        Label statusLabel = new Label("Conectado • 5 usuários online");
        statusLabel.setFont(Font.font("Segoe UI", 12));
        statusLabel.setTextFill(Color.web("#D1C4E9"));

        roomInfo.getChildren().addAll(roomLabel, statusLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox connectionStatus = new HBox(5);
        connectionStatus.setAlignment(Pos.CENTER_RIGHT);

        Label dot = new Label("●");
        dot.setTextFill(Color.LIMEGREEN);
        dot.setFont(Font.font(16));

        Label connLabel = new Label("Conectado");
        connLabel.setTextFill(Color.WHITE);
        connLabel.setFont(Font.font("Segoe UI", 12));

        connectionStatus.getChildren().addAll(dot, connLabel);
        topBar.getChildren().addAll(backButton, roomInfo, spacer, connectionStatus);
        chatHeader.getChildren().add(topBar);
        chatPane.setTop(chatHeader);

        // CENTRO - Área de chat
        SplitPane chatSplit = new SplitPane();
        chatSplit.setDividerPositions(0.8);

        // Painel principal de mensagens
        VBox chatAreaContainer = new VBox();
        chatAreaContainer.setPadding(new Insets(20, 20, 20, 20));

        // Área de mensagens
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setStyle("-fx-control-inner-background: white; " +
                "-fx-background-radius: 15; -fx-font-family: 'Segoe UI'; " +
                "-fx-font-size: 14px;");
        chatArea.setText("[14:30] Ana: Olá pessoal!\n" +
                "[14:31] Carlos: Tudo bem?\n" +
                "[14:32] Você: Estou testando o chat!");

        VBox.setVgrow(chatArea, Priority.ALWAYS);

        // Área de entrada
        HBox inputArea = new HBox(10);
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setPadding(new Insets(15, 0, 0, 0));

        TextField messageField = new TextField();
        messageField.setPromptText("Digite sua mensagem...");
        messageField.setStyle("-fx-background-color: white; -fx-background-radius: 25; " +
                "-fx-padding: 10 20; -fx-font-size: 14px;");
        HBox.setHgrow(messageField, Priority.ALWAYS);

        Button sendButton = new Button("Enviar");
        sendButton.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 25;");

        inputArea.getChildren().addAll(messageField, sendButton);

        // Barra de status
        HBox statusBar = new HBox(15);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 10;");

        Label tipLabel = new Label("💡 Pressione Enter para enviar");
        tipLabel.setFont(Font.font("Segoe UI", 12));
        tipLabel.setTextFill(Color.web("#666666"));

        Region statusSpacer = new Region();
        HBox.setHgrow(statusSpacer, Priority.ALWAYS);

        Label typingLabel = new Label("");
        typingLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        typingLabel.setTextFill(Color.web("#667eea"));

        statusBar.getChildren().addAll(tipLabel, statusSpacer, typingLabel);

        chatAreaContainer.getChildren().addAll(chatArea, inputArea, statusBar);

        // Painel de participantes
        VBox participantsPanel = new VBox(10);
        participantsPanel.setPadding(new Insets(20, 0, 20, 20));

        VBox participantsCard = new VBox(15);
        participantsCard.setPadding(new Insets(20));
        participantsCard.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(15), Insets.EMPTY)));
        participantsCard.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 15, 0, 0, 2);");

        Label participantsLabel = new Label("👥 Participantes (5)");
        participantsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        participantsLabel.setTextFill(Color.web("#333333"));

        // Lista de usuários
        VBox usersList = new VBox(8);
        String[] users = {"Ana (Você)", "Carlos", "Maria", "João", "Pedro"};

        for (String user : users) {
            HBox userItem = new HBox(10);
            userItem.setAlignment(Pos.CENTER_LEFT);
            userItem.setPadding(new Insets(8, 12, 8, 12));
            userItem.setStyle("-fx-background-radius: 8;");

            Label userName = new Label(user);
            userName.setFont(Font.font("Segoe UI", 14));
            userName.setTextFill(Color.web("#333333"));

            Label statusDot = new Label("●");
            statusDot.setTextFill(user.contains("Você") ? Color.web("#4CAF50") : Color.web("#4CAF50"));
            statusDot.setFont(Font.font(12));

            Region userSpacer = new Region();
            HBox.setHgrow(userSpacer, Priority.ALWAYS);

            userItem.getChildren().addAll(userName, userSpacer, statusDot);
            usersList.getChildren().add(userItem);
        }

        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        // Estatísticas
        VBox statsBox = new VBox(8);
        Label statsLabel = new Label("📊 Estatísticas");
        statsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        statsLabel.setTextFill(Color.web("#333333"));

        HBox onlineStat = new HBox(10);
        onlineStat.setAlignment(Pos.CENTER_LEFT);
        Label onlineText = new Label("Online:");
        onlineText.setFont(Font.font("Segoe UI", 12));
        onlineText.setTextFill(Color.web("#666666"));

        Label onlineCount = new Label("5");
        onlineCount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        onlineCount.setTextFill(Color.web("#667eea"));

        onlineStat.getChildren().addAll(onlineText, onlineCount);

        HBox messagesStat = new HBox(10);
        messagesStat.setAlignment(Pos.CENTER_LEFT);
        Label messagesText = new Label("Mensagens:");
        messagesText.setFont(Font.font("Segoe UI", 12));
        messagesText.setTextFill(Color.web("#666666"));

        Label messagesCount = new Label("15");
        messagesCount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        messagesCount.setTextFill(Color.web("#667eea"));

        messagesStat.getChildren().addAll(messagesText, messagesCount);

        statsBox.getChildren().addAll(statsLabel, onlineStat, messagesStat);
        participantsCard.getChildren().addAll(participantsLabel, usersList, separator, statsBox);

        participantsPanel.getChildren().add(participantsCard);
        chatSplit.getItems().addAll(chatAreaContainer, participantsPanel);
        chatPane.setCenter(chatSplit);

        // Ações
        backButton.setOnAction(e -> showMainScreen());

        sendButton.setOnAction(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
                chatArea.appendText("\n[" + time + "] Você: " + message);
                messageField.clear();
                chatArea.setScrollTop(Double.MAX_VALUE);
            }
        });

        messageField.setOnAction(e -> sendButton.fire());

        Scene scene = new Scene(chatPane, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}