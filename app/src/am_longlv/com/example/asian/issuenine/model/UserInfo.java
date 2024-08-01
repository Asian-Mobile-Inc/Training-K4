package com.example.asian.issuenine.model;
public class UserInfo {
    private final long mUserId;
    private final String mUsername;
    private final String mAge;

    public UserInfo(long mUserId, String mUsername, String mAge) {
        this.mUserId = mUserId;
        this.mUsername = mUsername;
        this.mAge = mAge;
    }

    public long getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getAge() {
        return mAge;
    }
}
