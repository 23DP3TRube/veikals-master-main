package tr.rvt;

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