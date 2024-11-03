package org.jetbrains.finguard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TransactionController {

    @FXML
    private ComboBox<String> accountNameComboBox;

    @FXML
    private ComboBox<String> transactionTypeComboBox;

    @FXML
    private TextField amountField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private Map<String, Integer> accountNameToIdMap = new HashMap<>();
    private Map<String, Integer> transactionTypeToIdMap = new HashMap<>();

    @FXML
    public void initialize() {
        populateAccountNames();
        populateTransactionTypes();
    }

    private void populateAccountNames() {
        ObservableList<String> accountNames = FXCollections.observableArrayList();
        for (Account account : databaseHandler.getAccounts()) {
            System.out.println("Account Name: " + account.getName());  // Debugging line
            accountNames.add(account.getName());
            accountNameToIdMap.put(account.getName(), account.getId());
        }
        accountNameComboBox.setItems(accountNames);
    }

    private void populateTransactionTypes() {
        ObservableList<String> transactionTypes = FXCollections.observableArrayList();
        for (TransactionType type : databaseHandler.getTransactionTypes()) {
            System.out.println("Transaction Type: " + type.getName());  // Debugging line
            transactionTypes.add(type.getName());
            transactionTypeToIdMap.put(type.getName(), type.getId());
        }
        transactionTypeComboBox.setItems(transactionTypes);
    }

    @FXML
    private void handleSaveTransaction() {
        String accountName = accountNameComboBox.getValue();
        String transactionTypeName = transactionTypeComboBox.getValue();
        String amountText = amountField.getText();

        if (accountName == null || transactionTypeName == null || amountText.isEmpty()) {
            showAlert("Input Error", "Please fill out all fields.");
            return;
        }

        try {
            int accountId = accountNameToIdMap.get(accountName);
            int transactionTypeId = transactionTypeToIdMap.get(transactionTypeName);
            double amount = Double.parseDouble(amountText);
            String currentDate = java.time.LocalDate.now().toString();

            if (databaseHandler.insertTransaction(accountId, transactionTypeId, amount, currentDate)) {
                showAlert("Success", "Transaction added successfully.");
                navigateToTransactionList(accountId);  // Navigate to transaction list
            } else {
                showAlert("Error", "Failed to add transaction.");
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid number for the amount.");
        }
    }

    private void navigateToTransactionList(int accountId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jetbrains/finguard/transaction-list.fxml"));
            Parent root = loader.load();

            TransactionListController controller = loader.getController();
            controller.initialize(accountId);

            Stage stage = (Stage) accountNameComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transaction List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load the Transaction List page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
