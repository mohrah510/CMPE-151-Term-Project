package org.jetbrains.finguard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountPageController {

    @FXML
    private TableView<Account> accountTable;

    @FXML
    private TableColumn<Account, String> nameColumn;

    @FXML
    private TableColumn<Account, Double> balanceColumn;

    @FXML
    private TableColumn<Account, String> createdAtColumn;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();

    public void initialize() {
        // Set up columns in the table
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Load data from the database
        accountTable.setItems(getAccountData());
    }

    private ObservableList<Account> getAccountData() {
        ObservableList<Account> accountList = FXCollections.observableArrayList();
        String query = "SELECT name, balance, created_at FROM accounts";

        try (Statement stmt = databaseHandler.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");
                String createdAt = rs.getString("created_at");
                accountList.add(new Account(name, balance, createdAt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountList;
    }
}
