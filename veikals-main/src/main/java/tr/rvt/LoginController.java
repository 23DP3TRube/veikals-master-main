package tr.rvt;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
        if (user != null && user.password.equals(password)) {
            App.setCurrentUser(user);
            System.out.println("Login successful!");
            App.setRoot("storeMenu");
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("mainMenu");
    }
}