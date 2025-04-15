package tr.rvt;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

public class StoreMenuController {

    @FXML
    private void handleViewItems() {
        StringBuilder itemsList = new StringBuilder("Available items:\n");
        for (int i = 0; i < App.getItems().size(); i++) {
            itemsList.append((i + 1)).append(". ").append(App.getItems().get(i)).append("\n");
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Items");
        alert.setHeaderText("Available Items");
        alert.setContentText(itemsList.toString());
        alert.showAndWait();
    }

    @FXML
    private void handleAddToCart() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add to Cart");
        dialog.setHeaderText("Enter the item number to add to the cart:");
        dialog.setContentText("Item number:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int itemIndex = Integer.parseInt(input) - 1;
                if (itemIndex >= 0 && itemIndex < App.getItems().size()) {
                    App.getCurrentUser().cart.add(App.getItems().get(itemIndex));
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Item Added");
                    alert.setContentText("Item added to your cart.");
                    alert.showAndWait();
                } else {
                    showError("Invalid item number.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        });
    }

    @FXML
    private void handleViewCart() {
        StringBuilder cartList = new StringBuilder("Your cart:\n");
        if (App.getCurrentUser().cart.isEmpty()) {
            cartList.append("Cart is empty.");
        } else {
            for (String item : App.getCurrentUser().cart) {
                cartList.append(item).append("\n");
            }
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Cart");
        alert.setHeaderText("Your Cart");
        alert.setContentText(cartList.toString());
        alert.showAndWait();
    }

    @FXML
    private void handleAddBalance() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Balance");
        dialog.setHeaderText("Enter the amount to add to your balance:");
        dialog.setContentText("Amount:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) {
                    App.getCurrentUser().balance += amount;
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Balance Updated");
                    alert.setContentText(String.format("Your new balance is $%.2f", App.getCurrentUser().balance));
                    alert.showAndWait();
                } else {
                    showError("Amount must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        });
    }

    @FXML
    private void handlePurchase() {
        if (App.getCurrentUser().cart.isEmpty()) {
            showError("Your cart is empty.");
            return;
        }

        double total = 0;
        for (String item : App.getCurrentUser().cart) {
            total += Double.parseDouble(item.split("- \\$")[1]);
        }

        if (App.getCurrentUser().balance >= total) {
            App.getCurrentUser().balance -= total;
            App.getCurrentUser().cart.clear();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Purchase Successful");
            alert.setHeaderText("Thank you for your purchase!");
            alert.setContentText(String.format("Your remaining balance is $%.2f", App.getCurrentUser().balance));
            alert.showAndWait();
        } else {
            showError("Insufficient balance.");
        }
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setCurrentUser(null);
        App.setRoot("mainMenu");
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}