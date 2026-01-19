module com.chat.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires javax.websocket.api;
    requires tyrus.client;
    requires tyrus.container.grizzly.client;
    requires java.logging;

    opens com.chat.client to javafx.fxml;
    opens com.chat.client.controller to javafx.fxml;
    opens com.chat.client.model to com.fasterxml.jackson.databind, javafx.base;

    exports com.chat.client;
    exports com.chat.client.controller;
    exports com.chat.client.model;
    exports com.chat.client.service;
}