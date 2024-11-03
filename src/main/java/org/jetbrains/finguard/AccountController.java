package org.jetbrains.finguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountController {

    @FXML
    private TextField accountNameField;

    @FXML
    private TextField balanceField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    private void handleSaveAccount() {
        String name = accountNameField.getText();
        String balanceText = balanceField.getText();

        if (name.isEmpty() || balanceText.isEmpty()) {
            showAlert("Input Error", "Please fill out all fields.");
            return;
        }

        try {
            double balance = Double.parseDouble(balanceText);
            String currentDate = java.time.LocalDate.now().toString();

            if (databaseHandler.insertAccount(name, balance, currentDate)) {
                showAlert("Success", "Account created successfully.");
                navigateToAccountPage();
            } else {
                showAlert("Error", "An account with this username already exists. Please choose a different username.");
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid number for the balance.");
        }
    }


    private void navigateToAccountPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jetbrains/finguard/account-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) accountNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load the account page.");
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
