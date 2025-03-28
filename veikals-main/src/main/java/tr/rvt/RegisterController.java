package tr.rvt;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (App.getUsers().containsKey(username)) {
            System.out.println("Username already exists!");
        } else {
            App.getUsers().put(username, new User(username, password));
            System.out.println("Registration successful!");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("mainMenu");
    }
}