package com.example.asian.model;

public class Item {
    private static int mIncrement = 0;
    private int mId;
    private String mName;

    public Item(String name) {
        mIncrement++;
        this.mId = mIncrement;
        this.mName = name;
    }

    public Item(int id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
