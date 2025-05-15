# User Guide for Veikals (Store) Application

## Introduction

Veikals is a simple JavaFX-based store application that allows users to register, log in, browse and sort products, manage a shopping cart, add balance, and make purchases. The application is designed for educational purposes and demonstrates basic e-commerce functionality in a desktop environment.

## Main Features

- **User Registration and Login:** Securely register a new account or log in with existing credentials.
- **Product Catalog:** Browse a list of available items, each with an ID, name, price, and quantity.
- **Sorting:** Sort items and cart contents by name, price, quantity, or ID, in both ascending and descending order.
- **Shopping Cart:** Add items to your cart, view and sort cart contents, and manage quantities.
- **Balance Management:** Add funds to your account balance.
- **Purchasing:** Buy all items in your cart if you have sufficient balance.
- **Logout:** Securely log out and return to the main menu.

## Interface Overview

- **Main Menu:** Options to Register, Login, or Exit.
- **Registration/Login Screens:** Enter username and password to register or log in.
- **Store Menu:** Access to view items, add to cart, view cart, add balance, purchase, sort items, and logout.
- **Dialogs:** Pop-up dialogs for sorting, adding to cart, adding balance, and purchase confirmations.

## Function Descriptions

- **Register:** Create a new user account by providing a unique username and password.
- **Login:** Access your account using your username and password.
- **View Items:** Display all available products. You can choose to sort the list by various criteria.
- **Sort Items:** Sort the product list by name, price, quantity, or ID, in ascending or descending order.
- **Add to Cart:** Enter the ID of an item to add it to your cart (if in stock).
- **View Cart:** See all items in your cart, with options to sort by name, price, quantity, or ID.
- **Add Balance:** Increase your account balance by entering an amount.
- **Purchase:** Buy all items in your cart if you have enough balance; your cart will be cleared after purchase.
- **Logout:** End your session and return to the main menu.

## How to Get Started

1. **Requirements:**  
   - Java 17 or newer  
   - JavaFX libraries (if not bundled with your JDK)

2. **Setup:**  
   - Download or clone the repository to your computer.
   - Ensure the `users.txt` file is present in the root directory for user data.

3. **Running the Application:**  
   - Open the project in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse, NetBeans).
   - Build and run the `App.java` file.
   - The main menu will appear.

4. **Using the Application:**  
   - Register a new account or log in with an existing one.
   - Once logged in, use the store menu to browse, sort, and purchase items.
   - Use the "Logout" button to end your session.

---

Enjoy using Veikals!
