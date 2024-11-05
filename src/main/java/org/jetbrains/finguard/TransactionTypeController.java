package org.jetbrains.finguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

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

    @FXML
    private void handleBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jetbrains/finguard/home-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) typeNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load the Home page.");
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
