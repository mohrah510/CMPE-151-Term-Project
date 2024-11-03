package org.jetbrains.finguard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionListController {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    public void initialize(int accountId) {
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        transactionTable.setItems(getTransactionData(accountId));
    }

    private ObservableList<Transaction> getTransactionData(int accountId) {
        ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
        String query = "SELECT transaction_types.type_name, transactions.amount, transactions.date " +
                "FROM transactions " +
                "JOIN transaction_types ON transactions.transaction_type_id = transaction_types.id " +
                "WHERE transactions.account_id = ?";

        try (PreparedStatement pstmt = databaseHandler.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String transactionType = rs.getString("type_name");
                double amount = rs.getDouble("amount");
                String date = rs.getString("date");
                transactionList.add(new Transaction(transactionType, amount, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionList;
    }
}
