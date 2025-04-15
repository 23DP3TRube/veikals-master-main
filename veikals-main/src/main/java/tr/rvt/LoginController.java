package tr.rvt;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = App.getUsers().get(username);
        Alert alert = new Alert(AlertType.INFORMATION);
        if (user != null && user.password.equals(password)) {
            App.setCurrentUser(user);
            alert.setTitle("Login Successful");
            alert.setHeaderText("Welcome, " + username + "!");
            alert.setContentText("You have successfully logged in.");
            App.setRoot("storeMenu");
        } else {
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid username or password!");
            alert.setContentText("Please try again.");
        }
        alert.showAndWait();
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("mainMenu");
    }
}