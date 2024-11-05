package org.jetbrains.finguard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransactionController {

    @FXML
    private ComboBox<String> accountNameComboBox;

    @FXML
    private ComboBox<String> transactionTypeComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker transactionDatePicker;  // New DatePicker for transaction date

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private Map<String, Integer> accountNameToIdMap = new HashMap<>();
    private Map<String, Integer> transactionTypeToIdMap = new HashMap<>();

    // Set of transaction types that increase the balance
    private static final Set<String> positiveTransactionTypes = new HashSet<>();

    static {
        positiveTransactionTypes.add("Deposit");
        positiveTransactionTypes.add("Salary");
    }

    @FXML
    public void initialize() {
        populateAccountNames();
        populateTransactionTypes();
    }

    private void populateAccountNames() {
        ObservableList<String> accountNames = FXCollections.observableArrayList();
        for (Account account : databaseHandler.getAccounts()) {
            accountNames.add(account.getName());
            accountNameToIdMap.put(account.getName(), account.getId());
        }
        accountNameComboBox.setItems(accountNames);
    }

    private void populateTransactionTypes() {
        ObservableList<String> transactionTypes = FXCollections.observableArrayList();
        for (TransactionType type : databaseHandler.getTransactionTypes()) {
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
        LocalDate transactionDate = transactionDatePicker.getValue();

        if (accountName == null || transactionTypeName == null || amountText.isEmpty() || transactionDate == null) {
            showAlert("Input Error", "Please fill out all fields.");
            return;
        }

        try {
            int accountId = accountNameToIdMap.get(accountName);
            int transactionTypeId = transactionTypeToIdMap.get(transactionTypeName);
            double amount = Double.parseDouble(amountText);

            // Check if the transaction type is positive; if not, make amount negative
            if (!positiveTransactionTypes.contains(transactionTypeName)) {
                amount = -amount;
            }

            String formattedDate = transactionDate.toString();  // Format LocalDate to string

            if (databaseHandler.insertTransaction(accountId, transactionTypeId, amount, formattedDate)) {
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

    @FXML
    private void handleBackToHome() {
        navigateToHomePage();
    }

    private void navigateToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jetbrains/finguard/home-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) accountNameComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
