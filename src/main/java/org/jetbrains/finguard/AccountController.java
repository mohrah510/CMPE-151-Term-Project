package org.jetbrains.finguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class AccountController {

    @FXML
    private TextField accountNameField;

    @FXML
    private TextField balanceField;

    @FXML
    private DatePicker createdAtDatePicker;  // New DatePicker for creation date

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    private void handleSaveAccount() {
        String name = accountNameField.getText();
        String balanceText = balanceField.getText();
        LocalDate createdAt = createdAtDatePicker.getValue();

        if (name.isEmpty() || balanceText.isEmpty() || createdAt == null) {
            showAlert("Input Error", "Please fill out all fields.");
            return;
        }

        try {
            double balance = Double.parseDouble(balanceText);
            String creationDate = createdAt.toString();  // Format LocalDate to string

            if (databaseHandler.insertAccount(name, balance, creationDate)) {
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

    @FXML
    private void handleBackToHome() {
        navigateToHomePage();
    }

    private void navigateToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jetbrains/finguard/home-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) accountNameField.getScene().getWindow();
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
