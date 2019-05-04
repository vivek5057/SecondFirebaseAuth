package com.example.secondfirebaseauth;

public class MessageContainer {

    public String chatName;
    public String chatMessage;
    public String chatImage;

    public MessageContainer() { }


    public MessageContainer(String chatName, String chatMessage,String chatImage) {
        this.chatName = chatName;
        this.chatMessage = chatMessage;
        this.chatImage = chatImage;
    }

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
