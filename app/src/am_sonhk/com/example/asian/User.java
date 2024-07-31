package com.example.asian;

public class User {
    private int userId;
    private String userName;
    private int age;

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
