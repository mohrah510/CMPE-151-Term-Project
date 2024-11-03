package org.jetbrains.finguard;

public class Account {
    private int id;
    private String name;
    private double balance;
    private String createdAt;

    // Constructor for use in ComboBox (id and name only)
    public Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor for creating new accounts
    public Account(String name, double balance, String createdAt) {
        this.name = name;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
