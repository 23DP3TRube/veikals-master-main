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