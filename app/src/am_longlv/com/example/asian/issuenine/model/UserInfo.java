package com.example.asian.issuenine.model;
public class UserInfo {
    private long mUserId;
    private String mUsername;
    private String mAge;

    public UserInfo(long mUserId, String mUsername, String mAge) {
        this.mUserId = mUserId;
        this.mUsername = mUsername;
        this.mAge = mAge;
    }

    public long getmUserId() {
        return mUserId;
    }

    public void setmUserId(long mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }
}
