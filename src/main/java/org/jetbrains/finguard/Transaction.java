package org.jetbrains.finguard;

public class Transaction {
    private String transactionType;
    private double amount;
    private String date;

    public Transaction(String transactionType, double amount, String date) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
