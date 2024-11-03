package org.jetbrains.finguard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static final String DATABASE_URL = "jdbc:sqlite:finGuard.db";
    private Connection connection;

    public DatabaseHandler() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            createTables();
            insertSampleTransactionTypes();  // Insert sample transaction types if needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE, " +
                "balance REAL NOT NULL, " +
                "created_at TEXT NOT NULL" +
                ");";

        String createTransactionTypesTable = "CREATE TABLE IF NOT EXISTS transaction_types (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type_name TEXT NOT NULL UNIQUE" +
                ");";

        String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "account_id INTEGER, " +
                "transaction_type_id INTEGER, " +
                "amount REAL NOT NULL, " +
                "date TEXT NOT NULL, " +
                "FOREIGN KEY(account_id) REFERENCES accounts(id), " +
                "FOREIGN KEY(transaction_type_id) REFERENCES transaction_types(id)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionTypesTable);
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertSampleTransactionTypes() {
        String checkQuery = "SELECT COUNT(*) FROM transaction_types";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {

            if (rs.next() && rs.getInt(1) == 0) {  // Table is empty, so insert sample data

                String[] transactionTypes = {
                        "Deposit", "Withdrawal", "Groceries", "Rent", "Salary", "Utilities",
                        "Transportation", "Entertainment", "Dining Out", "Insurance",
                        "Medical Expenses", "Education", "Shopping", "Debt Payment", "Investment"
                };

                String insertSQL = "INSERT INTO transaction_types (type_name) VALUES (?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                    for (String type : transactionTypes) {
                        pstmt.setString(1, type);
                        pstmt.executeUpdate();
                    }
                }
                System.out.println("Sample transaction types inserted into the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean insertAccount(String name, double balance, String date) {
        String insertSQL = "INSERT INTO accounts (name, balance, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, balance);
            pstmt.setString(3, date);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("Account creation failed: Username already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean insertTransactionType(String typeName) {
        String insertSQL = "INSERT INTO transaction_types (type_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, typeName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertTransaction(int accountId, int transactionTypeId, double amount, String date) {
        String insertSQL = "INSERT INTO transactions (account_id, transaction_type_id, amount, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, transactionTypeId);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT id, name FROM accounts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                accounts.add(new Account(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public List<TransactionType> getTransactionTypes() {
        List<TransactionType> types = new ArrayList<>();
        String query = "SELECT id, type_name FROM transaction_types";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                types.add(new TransactionType(rs.getInt("id"), rs.getString("type_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }
}