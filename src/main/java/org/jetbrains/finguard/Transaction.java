package org.jetbrains.finguard;

public class Transaction {
    private String transactionType;
    private double amount;
    private double previousBalance;
    private double currentBalance;
    private String date;

    public Transaction(String transactionType, double amount, double previousBalance, double currentBalance, String date) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.previousBalance = previousBalance;
        this.currentBalance = currentBalance;
        this.date = date;
    }

    // Getters for all fields
    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public String getDate() {
        return date;
    }
}
