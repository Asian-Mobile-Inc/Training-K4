package com.example.asian;

public class User {
    private final int mUserId;
    private final String mUserName;
    private final int mAge;

    public User(int userId, String userName, int age) {
        this.mUserId = userId;
        this.mUserName = userName;
        this.mAge = age;
    }


    public String getUserName() {
        return mUserName;
    }

    public int getAge() {
        return mAge;
    }

    public int getUserId() {
        return mUserId;
    }
}
