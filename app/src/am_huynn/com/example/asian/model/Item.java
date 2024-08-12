package com.example.asian.model;

public class Item {
    private static int increment = 0;
    private int id;
    private String name;

    public Item(String name) {
        increment++;
        this.id = increment;
        this.name = name;
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
