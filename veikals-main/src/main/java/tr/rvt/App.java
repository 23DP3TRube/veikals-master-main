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
    private static final List<Item> items = Arrays.asList(
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
    );
    private static User currentUser = null;
    private static final String USERS_FILE = "users.txt";

    // JavaFX aplikācijas sākums
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mainMenu"), 640, 480);
        scene.getStylesheets().add(getClass().getResource("/tr/rvt/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Maina skatu uz citu FXML
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Ielādē FXML failu
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Programmas ieejas punkts
    public static void main(String[] args) {
        loadUsers();
        launch();
    }

    // Atgriež lietotāju vārdnīcu
    public static Map<String, User> getUsers() {
        return users;
    }

    // Atgriež preču sarakstu
    public static List<Item> getItems() {
        return items;
    }

    // Atgriež pašreizējo lietotāju
    public static User getCurrentUser() {
        return currentUser;
    }

    // Uzstāda pašreizējo lietotāju
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Ielādē lietotājus no faila
    public static void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 3) {
                    String username = parts[0];
                    String password = parts[1];
                    double balance = 0.0;
                    try {
                        balance = Double.parseDouble(parts[2]);
                    } catch (NumberFormatException ignored) {}
                    User user = new User(username, password);
                    user.balance = balance;
                    if (parts.length >= 4 && !parts[3].isEmpty()) {
                        for (String entry : parts[3].split(";")) {
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

    // Saglabā visus lietotājus failā
    public static void saveAllUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, false))) {
            for (User user : users.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(escapeCsv(user.username)).append(",");
                sb.append(escapeCsv(user.password)).append(",");
                sb.append(user.balance).append(",");
                String cartStr = user.cart.stream()
                    .map(entry -> entry.item.id + ":" + entry.quantity)
                    .reduce((a, b) -> a + ";" + b).orElse("");
                sb.append("\"").append(cartStr).append("\"");
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    // Saglabā vienu lietotāju (un pārējos) failā
    public static void saveUser(User user) {
        users.put(user.username, user);
        saveAllUsers();
    }

    // Palīgfunkcija CSV lauka apstrādei
    private static String escapeCsv(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    // Palīgfunkcija CSV rindas parsēšanai ar pēdiņām
    private static String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }
}