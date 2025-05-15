package tr.rvt;

import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static final Map<String, User> users = new HashMap<>();
    private static final List<Item> items = new ArrayList<>(Arrays.asList(
        new Item(1, "Laptop", 800, 5),
        new Item(2, "Phone", 500, 10),
        new Item(3, "Headphones", 100, 15),
        new Item(4, "Smartwatch", 200, 8),
        new Item(5, "Tablet", 350, 7),
        new Item(6, "Monitor", 250, 6),
        new Item(7, "Keyboard", 50, 20),
        new Item(8, "Mouse", 30, 25),
        new Item(9, "Speaker", 120, 12),
        new Item(10, "Webcam", 70, 14),
        new Item(11, "Printer", 180, 4),
        new Item(12, "USB Drive", 20, 50)
    ));
    private static User currentUser = null;
    private static final String USERS_FILE = "users.txt";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mainMenu"), 640, 480);
        scene.getStylesheets().add(getClass().getResource("/tr/rvt/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        loadUsers();
        launch();
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    public static List<Item> getItems() {
        return items;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length >= 2) {
                    String username = parts[0];
                    String password = parts[1];
                    double balance = 0.0;
                    if (parts.length >= 3 && !parts[2].isEmpty()) {
                        try {
                            balance = Double.parseDouble(parts[2]);
                        } catch (NumberFormatException ignored) {}
                    }
                    User user = new User(username, password);
                    user.balance = balance;
                    // Load cart if present
                    if (parts.length == 4 && !parts[3].isEmpty()) {
                        String[] cartEntries = parts[3].split(";");
                        for (String entry : cartEntries) {
                            String[] itemParts = entry.split(":");
                            if (itemParts.length == 2) {
                                try {
                                    int itemId = Integer.parseInt(itemParts[0]);
                                    int qty = Integer.parseInt(itemParts[1]);
                                    Item item = items.stream().filter(i -> i.id == itemId).findFirst().orElse(null);
                                    if (item != null && qty > 0) {
                                        user.cart.add(new CartEntry(item, qty));
                                    }
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public static void saveAllUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, false))) {
            for (User user : users.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.username).append(",").append(user.password).append(",").append(user.balance).append(",");
                // Save cart as itemId:qty;itemId:qty
                for (int i = 0; i < user.cart.size(); i++) {
                    CartEntry entry = user.cart.get(i);
                    sb.append(entry.item.id).append(":").append(entry.quantity);
                    if (i < user.cart.size() - 1) sb.append(";");
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static void saveUser(User user) {
        users.put(user.username, user);
        saveAllUsers();
    }
}