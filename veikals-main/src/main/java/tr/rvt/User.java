package tr.rvt;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String password;
    public double balance;
    public List<CartEntry> cart;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.cart = new ArrayList<>();
    }
}
