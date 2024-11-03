package org.jetbrains.finguard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    @FXML
    private Button defineNewAccountButton; // Reference to a button on the home page

    @FXML
    private void handleDefineNewAccount() {
        navigateToDefineAccountPage();
    }

    @FXML
    private void handleCreateTransaction() {
        navigateToTransactionPage();
    }

    private void navigateToDefineAccountPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jetbrains/finguard/account.fxml"));
            Parent root = loader.load();

            // Get the current stage from the existing button on the home page
            Stage stage = (Stage) defineNewAccountButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Define New Account");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load the Define Account page.");
        }
    }

    private void navigateToTransactionPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jetbrains/finguard/transaction.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) defineNewAccountButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Transaction");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load the Create Transaction page.");
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
