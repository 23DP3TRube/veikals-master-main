package tr.rvt;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Alert alert = new Alert(AlertType.INFORMATION);
        if (App.getUsers().containsKey(username)) {
            alert.setTitle("Registration Failed");
            alert.setHeaderText("Username already exists!");
            alert.setContentText("Please choose a different username.");
        } else {
            User newUser = new User(username, password);
            App.getUsers().put(username, newUser);
            App.saveUser(newUser);
            alert.setTitle("Registration Successful");
            alert.setHeaderText("Welcome, " + username + "!");
            alert.setContentText("You have successfully registered.");
        }
        alert.showAndWait();
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("mainMenu");
    }
}