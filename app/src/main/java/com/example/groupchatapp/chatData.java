package com.example.groupchatapp;

public class chatData {
    String name;
    String key;
    String message;

    chatData() {}

    chatData(String name,
             String key,
             String message)
    {
        this.name = name;
        this.key = key;
        this.message = message;
    }

    public chatData(String message, String name) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "chatData{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}