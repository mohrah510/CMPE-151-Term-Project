package org.jetbrains.finguard;

public class TransactionType {
    private int id;
    private String name;

    public TransactionType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
