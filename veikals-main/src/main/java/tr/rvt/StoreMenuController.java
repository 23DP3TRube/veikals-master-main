package tr.rvt;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

public class StoreMenuController {

    // Parāda preču sarakstu ar kārtošanas un filtrēšanas iespējām
    @FXML
    private void handleViewItems() {
        List<String> sortOptions = List.of(
            "Name (A-Z)", "Name (Z-A)",
            "Price (Lowest to Highest)", "Price (Highest to Lowest)",
            "Quantity (Lowest to Highest)", "Quantity (Highest to Lowest)",
            "ID (Lowest to Highest)", "ID (Highest to Lowest)",
            "No Sorting"
        );
        ChoiceDialog<String> dialog = new ChoiceDialog<>("No Sorting", sortOptions);
        dialog.setTitle("Sort Items");
        dialog.setHeaderText("Choose how to sort the items:");
        dialog.setContentText("Sort by:");

        Optional<String> sortResult = dialog.showAndWait();
        List<Item> itemsToShow = App.getItems();

        if (sortResult.isPresent() && !sortResult.get().equals("No Sorting")) {
            switch (sortResult.get()) {
                case "Name (A-Z)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparing(item -> item.name.toLowerCase()))
                        .collect(Collectors.toList());
                    break;
                case "Name (Z-A)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparing((Item item) -> item.name.toLowerCase()).reversed())
                        .collect(Collectors.toList());
                    break;
                case "Price (Lowest to Highest)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparingInt(item -> item.price))
                        .collect(Collectors.toList());
                    break;
                case "Price (Highest to Lowest)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparingInt((Item item) -> item.price).reversed())
                        .collect(Collectors.toList());
                    break;
                case "Quantity (Lowest to Highest)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparingInt(item -> item.quantity))
                        .collect(Collectors.toList());
                    break;
                case "Quantity (Highest to Lowest)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparingInt((Item item) -> item.quantity).reversed())
                        .collect(Collectors.toList());
                    break;
                case "ID (Lowest to Highest)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparingInt(item -> item.id))
                        .collect(Collectors.toList());
                    break;
                case "ID (Highest to Lowest)":
                    itemsToShow = itemsToShow.stream()
                        .sorted(Comparator.comparingInt((Item item) -> item.id).reversed())
                        .collect(Collectors.toList());
                    break;
            }
        }

        TextInputDialog filterDialog = new TextInputDialog();
        filterDialog.setTitle("Filter Items");
        filterDialog.setHeaderText("Enter a keyword, ID, or price to filter items (leave empty for no filter):");
        filterDialog.setContentText("Filter:");
        Optional<String> filterResult = filterDialog.showAndWait();
        if (filterResult.isPresent() && !filterResult.get().trim().isEmpty()) {
            String filter = filterResult.get().trim().toLowerCase();
            itemsToShow = itemsToShow.stream()
                .filter(item ->
                    item.name.toLowerCase().contains(filter) ||
                    String.valueOf(item.id).equals(filter) ||
                    String.valueOf(item.price).equals(filter)
                )
                .collect(Collectors.toList());
        }

        StringBuilder itemsList = new StringBuilder("Available items:\n");
        for (Item item : itemsToShow) {
            itemsList.append(String.format("ID: %d | %s | $%d | Qty: %d\n",
                item.id, item.name, item.price, item.quantity));
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Items");
        alert.setHeaderText("Available Items");
        alert.setContentText(itemsList.toString());
        alert.showAndWait();
    }

    // Pievieno preci grozam pēc ID
    @FXML
    private void handleAddToCart() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add to Cart");
        dialog.setHeaderText("Enter the item ID to add to the cart:");
        dialog.setContentText("Item ID:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int itemId = Integer.parseInt(input);
                Item item = App.getItems().stream()
                    .filter(i -> i.id == itemId)
                    .findFirst()
                    .orElse(null);
                if (item != null && item.quantity > 0) {
                    CartEntry entry = App.getCurrentUser().cart.stream()
                        .filter(e -> e.item.id == itemId)
                        .findFirst()
                        .orElse(null);
                    if (entry != null) {
                        entry.quantity++;
                    } else {
                        App.getCurrentUser().cart.add(new CartEntry(item, 1));
                    }
                    item.quantity--;
                    App.saveUser(App.getCurrentUser());  
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Item Added");
                    alert.setContentText("Item added to your cart.");
                    alert.showAndWait();
                } else {
                    showError("Invalid item ID or item out of stock.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        });
    }

    // Noņem preci no groza pēc ID
    @FXML
    private void handleRemoveFromCart() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove from Cart");
        dialog.setHeaderText("Enter the item ID to remove from your cart:");
        dialog.setContentText("Item ID:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int itemId = Integer.parseInt(input);
                List<CartEntry> cart = App.getCurrentUser().cart;
                CartEntry entry = cart.stream()
                        .filter(e -> e.item.id == itemId)
                        .findFirst()
                        .orElse(null);
                if (entry != null) {
                    entry.item.quantity += entry.quantity;
                    cart.remove(entry);
                    App.saveUser(App.getCurrentUser());
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Removed");
                    alert.setHeaderText("Item Removed");
                    alert.setContentText("Item removed from your cart.");
                    alert.showAndWait();
                } else {
                    showError("Item not found in your cart.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        });
    }

    // Parāda groza saturu ar kārtošanas un filtrēšanas iespējām
    @FXML
    private void handleViewCart() {
        List<String> sortOptions = List.of(
            "Name (A-Z)", "Name (Z-A)",
            "Price (Lowest to Highest)", "Price (Highest to Lowest)",
            "Quantity (Lowest to Highest)", "Quantity (Highest to Lowest)",
            "ID (Lowest to Highest)", "ID (Highest to Lowest)",
            "No Sorting"
        );
        ChoiceDialog<String> dialog = new ChoiceDialog<>("No Sorting", sortOptions);
        dialog.setTitle("Sort Cart");
        dialog.setHeaderText("Choose how to sort your cart:");
        dialog.setContentText("Sort by:");

        Optional<String> sortResult = dialog.showAndWait();
        List<CartEntry> cartEntries = App.getCurrentUser().cart;

        List<CartEntry> entriesToShow = cartEntries;
        if (sortResult.isPresent() && !sortResult.get().equals("No Sorting")) {
            switch (sortResult.get()) {
                case "Name (A-Z)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparing(e -> e.item.name.toLowerCase()))
                        .collect(Collectors.toList());
                    break;
                case "Name (Z-A)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparing((CartEntry e) -> e.item.name.toLowerCase()).reversed())
                        .collect(Collectors.toList());
                    break;
                case "Price (Lowest to Highest)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparingInt(e -> e.item.price))
                        .collect(Collectors.toList());
                    break;
                case "Price (Highest to Lowest)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparingInt((CartEntry e) -> e.item.price).reversed())
                        .collect(Collectors.toList());
                    break;
                case "Quantity (Lowest to Highest)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparingInt(e -> e.quantity))
                        .collect(Collectors.toList());
                    break;
                case "Quantity (Highest to Lowest)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparingInt((CartEntry e) -> e.quantity).reversed())
                        .collect(Collectors.toList());
                    break;
                case "ID (Lowest to Highest)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparingInt(e -> e.item.id))
                        .collect(Collectors.toList());
                    break;
                case "ID (Highest to Lowest)":
                    entriesToShow = cartEntries.stream()
                        .sorted(Comparator.comparingInt((CartEntry e) -> e.item.id).reversed())
                        .collect(Collectors.toList());
                    break;
            }
        }

        TextInputDialog filterDialog = new TextInputDialog();
        filterDialog.setTitle("Filter Cart");
        filterDialog.setHeaderText("Enter a keyword, ID, or price to filter cart items (leave empty for no filter):");
        filterDialog.setContentText("Filter:");
        Optional<String> filterResult = filterDialog.showAndWait();
        if (filterResult.isPresent() && !filterResult.get().trim().isEmpty()) {
            String filter = filterResult.get().trim().toLowerCase();
            entriesToShow = entriesToShow.stream()
                .filter(entry ->
                    entry.item.name.toLowerCase().contains(filter) ||
                    String.valueOf(entry.item.id).equals(filter) ||
                    String.valueOf(entry.item.price).equals(filter)
                )
                .collect(Collectors.toList());
        }

        StringBuilder cartList = new StringBuilder("Your cart:\n");
        if (entriesToShow.isEmpty()) {
            cartList.append("Cart is empty.");
        } else {
            for (CartEntry entry : entriesToShow) {
                cartList.append(String.format("ID: %d | %s | $%d | Qty: %d\n",
                    entry.item.id, entry.item.name, entry.item.price, entry.quantity));
            }
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Cart");
        alert.setHeaderText("Your Cart");
        alert.setContentText(cartList.toString());
        alert.showAndWait();
    }

    // Pievieno naudu lietotāja bilancei
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
                    App.saveUser(App.getCurrentUser());
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

    // Parāda lietotāja bilanci
    @FXML
    private void handleCheckBalance() {
        double balance = App.getCurrentUser().balance;
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Balance");
        alert.setHeaderText("Your Balance");
        alert.setContentText(String.format("Your current balance is $%.2f", balance));
        alert.showAndWait();
    }

    // Veic pirkumu, ja pietiek bilances
    @FXML
    private void handlePurchase() {
        if (App.getCurrentUser().cart.isEmpty()) {
            showError("Your cart is empty.");
            return;
        }

        double total = App.getCurrentUser().cart.stream()
            .mapToDouble(entry -> entry.item.price * entry.quantity)
            .sum();

        if (App.getCurrentUser().balance >= total) {
            App.getCurrentUser().balance -= total;
            App.getCurrentUser().cart.clear();
            App.saveUser(App.getCurrentUser()); 
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Purchase Successful");
            alert.setHeaderText("Thank you for your purchase!");
            alert.setContentText(String.format("Your remaining balance is $%.2f", App.getCurrentUser().balance));
            alert.showAndWait();
        } else {
            showError("Insufficient balance.");
        }
    }

    // Izrakstās no lietotāja konta
    @FXML
    private void handleLogout() throws IOException {
        App.setCurrentUser(null);
        App.setRoot("mainMenu");
    }

    // Parāda preču kārtošanas iespējas
    @FXML
    private void handleSortItems() {
        List<String> options = List.of("A-Z", "Price", "Quantity", "ID");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("A-Z", options);
        dialog.setTitle("Sort Items");
        dialog.setHeaderText("Choose sorting method:");
        dialog.setContentText("Sort by:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            List<String> orderOptions = List.of("Ascending", "Descending");
            ChoiceDialog<String> orderDialog = new ChoiceDialog<>("Ascending", orderOptions);
            orderDialog.setTitle("Sort Order");
            orderDialog.setHeaderText("Choose sort order:");
            orderDialog.setContentText("Order:");
            Optional<String> orderResult = orderDialog.showAndWait();
            boolean ascending = !orderResult.isPresent() || orderResult.get().equals("Ascending");

            List<Item> sorted;
            switch (result.get()) {
                case "A-Z":
                    sorted = App.getItems().stream()
                        .sorted(ascending
                            ? Comparator.comparing(item -> item.name.toLowerCase())
                            : Comparator.comparing((Item item) -> item.name.toLowerCase()).reversed())
                        .collect(Collectors.toList());
                    break;
                case "Price":
                    sorted = App.getItems().stream()
                        .sorted(ascending
                            ? Comparator.comparingInt(item -> item.price)
                            : Comparator.comparingInt((Item item) -> item.price).reversed())
                        .collect(Collectors.toList());
                    break;
                case "Quantity":
                    sorted = App.getItems().stream()
                        .sorted(ascending
                            ? Comparator.comparingInt(item -> item.quantity)
                            : Comparator.comparingInt((Item item) -> item.quantity).reversed())
                        .collect(Collectors.toList());
                    break;
                case "ID":
                    sorted = App.getItems().stream()
                        .sorted(ascending
                            ? Comparator.comparingInt(item -> item.id)
                            : Comparator.comparingInt((Item item) -> item.id).reversed())
                        .collect(Collectors.toList());
                    break;
                default:
                    sorted = App.getItems();
            }
            showSortedItems(sorted, result.get() + " (" + (ascending ? "Ascending" : "Descending") + ")");
        }
    }

    // Parāda sakārtotu preču sarakstu
    private void showSortedItems(List<Item> items, String sortType) {
        StringBuilder itemsList = new StringBuilder("Items sorted by " + sortType + ":\n");
        for (Item item : items) {
            itemsList.append(String.format("ID: %d | %s | $%d | Qty: %d\n",
                item.id, item.name, item.price, item.quantity));
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sorted Items");
        alert.setHeaderText("Items sorted by " + sortType);
        alert.setContentText(itemsList.toString());
        alert.showAndWait();
    }

    // Parāda kļūdas paziņojumu
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }
}