package com.disasterresponse.controller;

import com.disasterresponse.model.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.disasterresponse.DatabaseConnection;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;

    private String userAccessLevel;
    private String fullName;
    private String userId;

  @FXML
    protected void handleLoginAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Please enter both username and password.");
            errorMessageLabel.setVisible(true);
            return;
        }

        if (authenticate(username, password)) {
            SessionManager.getInstance().setLoggedInUser(userId, username, userAccessLevel, fullName);
            loadHomepageView();
        } else {
            // Check if username exists
            if (!isUserExists(username)) {
                errorMessageLabel.setText("Invalid username.");
            } else {
                errorMessageLabel.setText("Invalid password.");
            }
            errorMessageLabel.setVisible(true);
        }
    }


    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getString("userId");
                userAccessLevel = rs.getString("AccessLevel");
                fullName = rs.getString("FullName");
                return true;
            } 

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors 
        }

        return false;
    }
     // Helper function to check if username exists
    private boolean isUserExists(String username) {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Username exists if a row is returned

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }
        return false; 
    }

    private void loadHomepageView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSignupAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/SignupView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleForgotPasswordAction() {
        // This is the missing event handler referenced in your FXML.
        // You can provide the actual implementation based on your use case.

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText(null);
        alert.setContentText("Forgot Password functionality will be implemented here.");
        alert.showAndWait();
    }
}
