import java.util.*;

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

public class App {
    private static final Map<String, User> users = new HashMap<>();
    private static final List<String> items = Arrays.asList("Laptop - $800", "Phone - $500", "Headphones - $100");
    private static User currentUser = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to the Store!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        users.put(username, new User(username, password));
        System.out.println("Registration successful!");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            currentUser = user;
            System.out.println("Login successful!");
            storeMenu();
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    private static void storeMenu() {
        while (true) {
            System.out.println("\nStore Menu");
            System.out.println("1. View Items");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Add Balance");
            System.out.println("5. Purchase");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewItems();
                case 2 -> addToCart();
                case 3 -> viewCart();
                case 4 -> addBalance();
                case 5 -> purchase();
                case 6 -> {
                    currentUser = null;
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewItems() {
        System.out.println("Available items:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }
    }

    private static void addToCart() {
        viewItems();
        System.out.print("Enter item number to add to cart: ");
        try {
            int itemIndex = scanner.nextInt() - 1;
            scanner.nextLine();
            if (itemIndex >= 0 && itemIndex < items.size()) {
                currentUser.cart.add(items.get(itemIndex));
                System.out.println("Item added to cart.");
            } else {
                System.out.println("Invalid item number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    private static void viewCart() {
        System.out.println("Your cart:");
        if (currentUser.cart.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            currentUser.cart.forEach(System.out::println);
        }
    }

    private static void addBalance() {
        System.out.print("Enter amount to add: ");
        try {
            double amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount > 0) {
                currentUser.balance += amount;
                System.out.printf("Balance updated. Current balance: $%.2f%n", currentUser.balance);
            } else {
                System.out.println("Invalid amount.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // clear the invalid input
        }
    }

    private static void purchase() {
        if (currentUser.cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        double total = 0;
        for (String item : currentUser.cart) {
            total += Double.parseDouble(item.split("- \\$")[1]);
        }

        if (currentUser.balance >= total) {
            currentUser.balance -= total;
            currentUser.cart.clear();
            System.out.printf("Purchase successful! Remaining balance: $%.2f%n", currentUser.balance);
        } else {
            System.out.println("Insufficient balance.");
        }
    }
}