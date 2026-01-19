package com.chat.model;

public class ChatRoom {
    private Long id;
    private String name;
    private String description;
    private Integer memberCount;
    private Boolean isPrivate;
    private String createdAt;

    public ChatRoom() {}

    public ChatRoom(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Para compatibilidade com TableView que espera userCount
    public Integer getUserCount() {
        return memberCount;
    }

    @Override
    public String toString() {
        return name + " (" + (memberCount != null ? memberCount : 0) + " usuários)";
    }

    public void setUserCount(int i) {
    }
}