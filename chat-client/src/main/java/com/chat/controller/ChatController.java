package com.chat.controller;

import com.chat.ChatClientApplication;
import com.chat.model.Message;
import com.chat.service.ApiService;
import com.chat.service.WebSocketService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import java.time.format.DateTimeFormatter;

public class ChatController {

    @FXML private TextArea chatArea;
    @FXML private TextField messageField;
    @FXML private Button sendButton;
    @FXML private Button backButton;
    @FXML private Label roomLabel;
    @FXML private Label statusLabel;
    @FXML private Label typingLabel;
    @FXML private Label onlineCountLabel;
    @FXML private Label messageCountLabel;
    @FXML private ListView<String> usersListView;

    private WebSocketService webSocketService;
    private ObservableList<String> users = FXCollections.observableArrayList();
    private static Long currentRoomId;
    private static String currentRoomName;
    private int messageCount = 0;

    @FXML
    private void initialize() {
        usersListView.setItems(users);
        roomLabel.setText("Sala: " + currentRoomName); // REMOVIDO EMOJI
        statusLabel.setText("Conectando...");
        typingLabel.setText("");
        onlineCountLabel.setText("0");
        messageCountLabel.setText("0");

        // Configurar ListView
        usersListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String username, boolean empty) {
                super.updateItem(username, empty);
                if (empty || username == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    hbox.setPadding(new Insets(8, 12, 8, 12));

                    Label userLabel = new Label(username);
                    userLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

                    Label statusDot = new Label("•");
                    statusDot.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 12px;");

                    hbox.getChildren().addAll(userLabel, statusDot);
                    setGraphic(hbox);
                }
            }
        });

        // Configurar WebSocket
        webSocketService = new WebSocketService();
        webSocketService.setMessageHandler(this::handleIncomingMessage);
        webSocketService.setStatusHandler(status -> {
            Platform.runLater(() -> statusLabel.setText(status));
        });

        // Conectar ao WebSocket
        String username = ApiService.getCurrentUsername();
        webSocketService.connect(username, currentRoomId);

        // Enviar mensagem com Enter
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSendMessage();
            }
        });

        // Configurar área de chat
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
    }

    private void handleIncomingMessage(Message message) {
        Platform.runLater(() -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String time = message.getTimestamp() != null ?
                    message.getTimestamp().format(formatter) : "Agora";

            String sender = message.getSender();
            String content = message.getContent();

            String simpleMessage = String.format("[%s] %s: %s\n", time, sender, content);
            chatArea.appendText(simpleMessage);

            // Scroll automático
            chatArea.setScrollTop(Double.MAX_VALUE);

            // Atualizar contador
            messageCount++;
            messageCountLabel.setText(String.valueOf(messageCount));
        });
    }

    @FXML
    private void handleSendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty()) {
            Message message = new Message(
                    ApiService.getCurrentUsername(),
                    content,
                    currentRoomId
            );

            webSocketService.sendMessage(message);
            messageField.clear();
            typingLabel.setText("");
        }
    }

    @FXML
    private void handleBack() {
        if (webSocketService != null) {
            webSocketService.disconnect();
        }
        ChatClientApplication.showMainScreen();
    }

    public void addUser(String username) {
        Platform.runLater(() -> {
            if (!users.contains(username)) {
                users.add(username);
                onlineCountLabel.setText(String.valueOf(users.size()));
            }
        });
    }

    public void removeUser(String username) {
        Platform.runLater(() -> {
            users.remove(username);
            onlineCountLabel.setText(String.valueOf(users.size()));
        });
    }

    public static void setRoomInfo(Long roomId, String roomName) {
        currentRoomId = roomId;
        currentRoomName = roomName;
    }
}