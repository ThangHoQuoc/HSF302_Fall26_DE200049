package edu.fpt.swt301.demologin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label welcomeText;

    @FXML
    private void initialize() {
        welcomeText.setText("");
    }

    @FXML
    protected void onHelloButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            welcomeText.setText("Please enter username and password!");
            return;
        }

        if ("admin".equals(username) && "123".equals(password)) {
            welcomeText.setText("Login successful! Welcome back.");
        } else {
            welcomeText.setText("Invalid username or password.");
        }
    }
}
