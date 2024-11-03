package org.jetbrains.finguard;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class TransactionTypeController {

    @FXML
    private TextField typeNameField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    private void handleSaveTransactionType() {
        String typeName = typeNameField.getText();

        if (typeName.isEmpty()) {
            showAlert("Input Error", "Please enter a transaction type.");
            return;
        }

        if (databaseHandler.insertTransactionType(typeName)) {
            showAlert("Success", "Transaction type added successfully.");
        } else {
            showAlert("Error", "Failed to add transaction type.");
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
