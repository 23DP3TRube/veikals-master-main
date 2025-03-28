package tr.rvt;

import java.io.IOException;

import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    private void handleRegister() throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void handleLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}