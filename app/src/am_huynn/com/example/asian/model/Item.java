package com.example.asian.model;

public class Item {
    private static int increment = 0;
    private int itemId;
    private String itemName;

    public Item(String itemName) {
        increment++;
        this.itemId = increment;
        this.itemName = itemName;
    }

    public Item(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }
}
