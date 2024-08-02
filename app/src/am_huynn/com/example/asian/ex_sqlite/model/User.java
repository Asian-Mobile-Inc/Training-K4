package com.example.asian.ex_sqlite.model;

public class User {
    final private int userId;
    final private String userName;
    final private int age;

    public User(int userId, String userName, int age) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }
}
