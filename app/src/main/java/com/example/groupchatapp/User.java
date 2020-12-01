package com.example.groupchatapp;

public class User {

    String name;
    String email;
    String uid;

    public User() {

    }

    public User(String name, String email, String uuid) {
        this.name = name;
        this.email = email;
        this.uid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uuid='" + uid + '\'' +
                '}';
    }
}
