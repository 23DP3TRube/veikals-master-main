package tr.rvt;

import java.io.IOException;

import javafx.fxml.FXML;

public class StoreMenuController {

    @FXML
    private void handleViewItems() {
        System.out.println("Available items:");
        for (int i = 0; i < App.getItems().size(); i++) {
            System.out.println((i + 1) + ". " + App.getItems().get(i));
        }
    }

    @FXML
    private void handleAddToCart() {
        // Implement add to cart functionality
    }

    @FXML
    private void handleViewCart() {
        // Implement view cart functionality
    }

    @FXML
    private void handleAddBalance() {
        // Implement add balance functionality
    }

    @FXML
    private void handlePurchase() {
        // Implement purchase functionality
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setCurrentUser(null);
        App.setRoot("mainMenu");
    }
}