package com.chat.controller;

import com.chat.ChatClientApplication;
import com.chat.model.ChatRoom;
import com.chat.model.User;
import com.chat.service.ApiService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class MainController {

    @FXML private TableView<ChatRoom> roomsTable;
    @FXML private TableColumn<ChatRoom, String> nameColumn;
    @FXML private TableColumn<ChatRoom, String> descriptionColumn;
    @FXML private TableColumn<ChatRoom, Integer> usersColumn;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, Boolean> statusColumn;

    @FXML private TextField roomNameField;
    @FXML private TextField roomDescriptionField;
    @FXML private Button createRoomButton;
    @FXML private Button joinRoomButton;
    @FXML private Button refreshButton;
    @FXML private Button logoutButton;
    @FXML private Label welcomeLabel;

    private ObservableList<ChatRoom> chatRooms = FXCollections.observableArrayList();
    private ObservableList<User> onlineUsers = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        welcomeLabel.setText("Bem-vindo, " + ApiService.getCurrentUsername() + "!");

        setupRoomsTable();
        setupUsersTable();
        loadData();
    }

    private void setupRoomsTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        usersColumn.setCellValueFactory(new PropertyValueFactory<>("userCount"));

        roomsTable.setItems(chatRooms);

        // Seleção de sala
        roomsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    joinRoomButton.setDisable(newSelection == null);
                });
    }

    private void setupUsersTable() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("online"));

        // Customizar coluna de status
        statusColumn.setCellFactory(col -> new TableCell<User, Boolean>() {
            @Override
            protected void updateItem(Boolean online, boolean empty) {
                super.updateItem(online, empty);
                if (empty || online == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(online ? "Online" : "Offline");
                    if (online) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: gray;");
                    }
                }
            }
        });

        usersTable.setItems(onlineUsers);
    }

    private void loadData() {
        loadChatRooms();
        loadOnlineUsers();
    }

    private void loadChatRooms() {
        // Dados de exemplo enquanto não conecta ao backend
        Platform.runLater(() -> {
            chatRooms.clear();
            chatRooms.addAll(List.of(
                    new ChatRoom("Geral", "Conversas gerais"),
                    new ChatRoom("Desenvolvimento", "Discussões técnicas"),
                    new ChatRoom("Social", "Conversas informais")
            ));
        });
    }

    private void loadOnlineUsers() {
        // Dados de exemplo
        Platform.runLater(() -> {
            onlineUsers.clear();
            User user1 = new User();
            user1.setUsername("Ana");
            user1.setOnline(true);

            User user2 = new User();
            user2.setUsername("Carlos");
            user2.setOnline(true);

            User user3 = new User();
            user3.setUsername("Maria");
            user3.setOnline(false);

            onlineUsers.addAll(user1, user2, user3);
        });
    }

    @FXML
    private void handleCreateRoom() {
        String name = roomNameField.getText().trim();
        String description = roomDescriptionField.getText().trim();

        if (name.isEmpty()) {
            showAlert("Erro", "Digite um nome para a sala!");
            return;
        }

        ChatRoom room = new ChatRoom(name, description);
        room.setUserCount(1);

        Platform.runLater(() -> {
            chatRooms.add(room);
            roomNameField.clear();
            roomDescriptionField.clear();
            showAlert("Sucesso", "Sala criada com sucesso!");
        });
    }

    @FXML
    private void handleJoinRoom() {
        ChatRoom selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            // Chama com o nome da sala como parâmetro
            ChatClientApplication.showChatScreen(selectedRoom.getName());
        }
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }

    @FXML
    private void handleLogout() {
        ChatClientApplication.showLoginScreen();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}