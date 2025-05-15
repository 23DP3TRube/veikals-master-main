package tr.rvt;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static final Map<String, User> users = new HashMap<>();
    private static final List<String> items = Arrays.asList("Laptop - $800", "Phone - $500", "Headphones - $100");
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

    public static List<String> getItems() {
        return items;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public static void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.username + "," + user.password);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }
}

class User {
    String username;
    String password;
    double balance;
    List<String> cart;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.cart = new ArrayList<>();
    }
}