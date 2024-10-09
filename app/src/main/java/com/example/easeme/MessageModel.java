package com.example.easeme;

public class MessageModel {
    String message,senderId;
    long timestamp;

    public MessageModel(String message, String senderId) {

        this.message = message;

        this.senderId = senderId;

    }

    public MessageModel() {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



}
