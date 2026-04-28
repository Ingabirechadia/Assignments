import java.io.*;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Customer loggedInCustomer = null;
    private static Cart activeCart = null;

    public static void main(String[] args) {
        System.out.println("\n============================================================");
        System.out.println("    CHADIA'S E-COMMERCE SYSTEM ");
        System.out.println("============================================================");


        System.out.println("\n Files will be saved in: " + System.getProperty("user.dir"));


        loadAllData();


        if (Product.getAllProducts().isEmpty()) {
            addDemoProducts();
        }
        while (true) {
            if (loggedInCustomer == null) {
                showMainMenu();
            } else {
                showCustomerMenu();
            }
        }
    }

    private static void loadAllData() {
        System.out.println("\n Loading saved data...");
        Product.loadFromFile();
    }

    private static void addDemoProducts() {
        System.out.println("\n Adding demo products...");
        try {
            new Product("Laptop", 50000, 5, "Electronics");
            new Product("Phone", 30000, 2, "Electronics");
            new Product("Desk", 15000, 8, "Furniture");
            new Product("Tomatoes", 2000, 100, "Fresh Produce");
            new Product("Peas", 1500, 75, "Fresh Produce");
            new Product("Organic Fertilizer", 3500, 50, "Supplies");
            Product.saveToFile();
            System.out.println("✓ Demo products added!");
        } catch (Exception e) {
            System.out.println("Error adding demo products: " + e.getMessage());
        }
    }

    private static void showMainMenu() {
        System.out.println("\n==================================================");
        System.out.println("MAIN MENU");
        System.out.println("==================================================");
        System.out.println(" 1.  Customer Login");
        System.out.println(" 2.  Register New Customer");
        System.out.println(" 3.  Admin Login");
        System.out.println(" 4.  Browse Products (Guest)");
        System.out.println(" 5.  Save & Exit");
        System.out.println("==================================================");

        System.out.print("\n Enter your choice: ");
        int choice = getIntInput();

        switch (choice) {
            case 1:
                customerLogin();
                break;
            case 2:
                registerCustomer();
                break;
            case 3:
                adminLogin();
                break;
            case 4:
                Product.printCatalog();
                break;
            case 5:
                saveAndExit();
                break;
            default:
                System.out.println(" Invalid choice. Please try again.");
        }
    }

    private static void customerLogin() {
        System.out.print("\n Enter customer name: ");
        String name = scanner.nextLine();

        Customer customer = Customer.loadFromFile(name);
        if (customer == null) {
            System.out.println(" Customer not found. Please register first.");
            return;
        }

        loggedInCustomer = customer;
        loggedInCustomer.loadOrderHistoryFromFile();
        activeCart = new Cart(loggedInCustomer.getName());

        System.out.println("\n Welcome back, " + loggedInCustomer.getName() + "!");
        System.out.println("    Balance: RWF " + loggedInCustomer.getBalance());
        System.out.println("    Cart has " + activeCart.getItemCount() + " items");
    }

    private static void registerCustomer() {
        System.out.println("\n--- REGISTER NEW CUSTOMER ---");
        System.out.print(" Customer Name: ");
        String name = scanner.nextLine();

        if (Customer.loadFromFile(name) != null) {
            System.out.println(" Customer already exists!");
            return;
        }

        double balance = getDoubleInput(" Initial Balance (RWF): ");

        try {
            Customer newCustomer = new Customer(name, balance);
            newCustomer.saveToFile();
            System.out.println("\n Customer registered successfully!");
            System.out.println("   You can now login with: " + name);
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private static void adminLogin() {
        System.out.print("\n Enter admin password: ");
        String password = scanner.nextLine();

        if (!password.equals("admin123")) {
            System.out.println(" Invalid password!");
            return;
        }

        showAdminMenu();
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n----------------------------------------");
            System.out.println("🔧 ADMIN MENU");
            System.out.println("----------------------------------------");
            System.out.println(" 1.  Add New Product");
            System.out.println(" 2.  View All Products");
            System.out.println(" 3.  Update Product Stock");
            System.out.println(" 4.  Remove Product");
            System.out.println(" 5.  View All Customers");
            System.out.println(" 6.  Export Reports to TXT");
            System.out.println(" 7.  Back to Main Menu");

            System.out.print("\nChoose: ");
            int choice = getIntInput();

            switch (choice) {
                case 1: addNewProduct(); break;
                case 2: Product.printCatalog(); break;
                case 3: updateProductStock(); break;
                case 4: removeProduct(); break;
                case 5: viewAllCustomers(); break;
                case 6: exportReports(); break;
                case 7: return;
                default: System.out.println(" Invalid choice.");
            }
        }
    }

    private static void addNewProduct() {
        System.out.println("\n--- ADD NEW PRODUCT ---");
        System.out.print(" Product Name: ");
        String name = scanner.nextLine();
        System.out.print(" Category: ");
        String category = scanner.nextLine();
        double price = getDoubleInput(" Price (RWF): ");
        int stock = getIntInput(" Stock Quantity: ");

        try {
            new Product(name, price, stock, category);
            Product.saveToFile();
            System.out.println("\n Product added successfully!");
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private static void updateProductStock() {
        System.out.print("\n Enter product name: ");
        String name = scanner.nextLine();

        Product product = Product.findProductByName(name);
        if (product == null) {
            System.out.println(" Product not found!");
            return;
        }

        System.out.println("Current stock: " + product.getStock());
        int newStock = getIntInput(" New stock quantity: ");
        product.setStock(newStock);
        Product.saveToFile();
        System.out.println(" Stock updated!");
    }

    private static void removeProduct() {
        System.out.print("\n Enter product name: ");
        String name = scanner.nextLine();
        System.out.print(" Enter category: ");
        String category = scanner.nextLine();
        Product.removeFromCatalog(category, name);
        Product.saveToFile();
    }

    private static void viewAllCustomers() {
        Map<String, Customer> customers = Customer.loadAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("\n No customers registered.");
        } else {
            System.out.println("\n REGISTERED CUSTOMERS:");
            System.out.println("--------------------------------------------------");
            for (Customer c : customers.values()) {
                System.out.printf("  • %-15s | Balance: RWF %8.2f | Orders: %d%n",
                        c.getName(), c.getBalance(), c.getOrderCount());
            }
        }
    }

    private static void exportReports() {
        System.out.println("\n Exporting reports to TXT files...");


        try (PrintWriter pw = new PrintWriter(new FileWriter("products_report.txt"))) {
            pw.println("=".repeat(60));
            pw.println("PRODUCTS REPORT");
            pw.println("=".repeat(60));
            pw.printf("%-25s %-15s %-10s %-10s%n", "Product Name", "Category", "Price", "Stock");
            pw.println("-".repeat(60));

            for (Product p : Product.getAllProducts()) {
                pw.printf("%-25s %-15s RWF %-8.2f %-10d%n",
                        p.getName(), p.getCategory(), p.getPrice(), p.getStock());
            }
            pw.println("=".repeat(60));
            pw.printf("Total Products: %d%n", Product.getAllProducts().size());
            pw.println("=".repeat(60));
            System.out.println("  ✓ products_report.txt");
        } catch (IOException e) {
            System.out.println("  ✗ Error exporting products");
        }


        try (PrintWriter pw = new PrintWriter(new FileWriter("customers_report.txt"))) {
            pw.println("=".repeat(60));
            pw.println("CUSTOMERS REPORT");
            pw.println("=".repeat(60));
            pw.printf("%-20s %-12s %-10s%n", "Customer Name", "Balance", "Orders");
            pw.println("-".repeat(60));

            for (Customer c : Customer.loadAllCustomers().values()) {
                pw.printf("%-20s RWF %-8.2f %-10d%n",
                        c.getName(), c.getBalance(), c.getOrderCount());
            }
            pw.println("=".repeat(60));
            System.out.println("  ✓ customers_report.txt");
        } catch (IOException e) {
            System.out.println("  ✗ Error exporting customers");
        }

        System.out.println("\n Export complete! Check your project folder for TXT files.");
    }


    private static void showCustomerMenu() {
        System.out.println("\n------------------------------------");
        System.out.println(" WELCOME " + loggedInCustomer.getName().toUpperCase());
        System.out.println("---------------------------------------");
        System.out.println(" 1.  Browse Products");
        System.out.println(" 2.  Add to Cart");
        System.out.println(" 3.  View Cart");
        System.out.println(" 4.  Remove from Cart");
        System.out.println(" 5.  Checkout");
        System.out.println(" 6.  My Order History");
        System.out.println(" 7.  My Preferences");
        System.out.println(" 8.  Add Balance");
        System.out.println(" 9.  Logout");

        System.out.print("\n Choose: ");
        int choice = getIntInput();

        switch (choice) {
            case 1:
                Product.printCatalog();
                break;
            case 2:
                addToCart();
                break;
            case 3:
                activeCart.printCart();
                break;
            case 4:
                removeFromCart();
                break;
            case 5:
                checkout();
                break;
            case 6:
                loggedInCustomer.printOrderHistory();
                break;
            case 7:
                managePreferences();
                break;
            case 8:
                addBalance();
                break;
            case 9:
                logout();
                break;
            default:
                System.out.println(" Invalid choice.");
        }
    }

    private static void addToCart() {
        Product.printCatalog();
        System.out.print("\n Enter product name: ");
        String name = scanner.nextLine();

        Product product = Product.findProductByName(name);
        if (product == null) {
            System.out.println(" Product not found!");
            return;
        }

        int quantity = getIntInput(" Enter quantity: ");

        try {
            activeCart.addProduct(product, quantity);
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private static void removeFromCart() {
        if (activeCart.isEmpty()) {
            System.out.println(" Cart is empty.");
            return;
        }
        activeCart.printCart();
        System.out.print("\n Enter product name to remove: ");
        String name = scanner.nextLine();
        activeCart.removeProduct(name);
    }

    private static void checkout() {
        if (activeCart.isEmpty()) {
            System.out.println("🛒 Cart is empty. Add items first.");
            return;
        }

        double total = activeCart.calculateTotal();
        activeCart.printCart();

        System.out.println("\n--- CHECKOUT SUMMARY ---");
        System.out.printf("  Total Amount: RWF %.2f%n", total);
        System.out.printf("  Your Balance: RWF %.2f%n", loggedInCustomer.getBalance());

        if (loggedInCustomer.getBalance() < total) {
            System.out.println("   Insufficient balance!");
            return;
        }

        System.out.print("\n Confirm purchase? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            try {
                Payment.processPayment(loggedInCustomer, total);

                String orderId = "ORD-" + System.currentTimeMillis();
                List<Double> amounts = new ArrayList<>();
                amounts.add(total);
                loggedInCustomer.recordOrder(orderId, amounts);

                activeCart.checkout();
                loggedInCustomer.saveToFile();
                Product.saveToFile();

                System.out.println("\n ORDER PLACED SUCCESSFULLY!");
                System.out.println("   Order ID: " + orderId);
                System.out.println("   Remaining Balance: RWF " + loggedInCustomer.getBalance());
            } catch (Exception e) {
                System.out.println(" Checkout failed: " + e.getMessage());
            }
        } else {
            System.out.println("Checkout cancelled.");
        }
    }

    private static void managePreferences() {
        System.out.println("\n--- PREFERRED CATEGORIES ---");
        System.out.println("Current: " + loggedInCustomer.getPreferredCategories());
        System.out.println("\n 1. Add category");
        System.out.println(" 2. Remove category");
        System.out.println(" 3. Back");

        System.out.print("Choose: ");
        int choice = getIntInput();

        switch (choice) {
            case 1:
                System.out.print("Enter category name: ");
                String cat = scanner.nextLine();
                loggedInCustomer.addPreferredCategory(cat);
                break;
            case 2:
                System.out.print("Enter category to remove: ");
                String removeCat = scanner.nextLine();
                loggedInCustomer.removePreferredCategory(removeCat);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
        loggedInCustomer.saveToFile();
    }

    private static void addBalance() {
        double amount = getDoubleInput(" Enter amount to add: ");
        loggedInCustomer.addBalance(amount);
        loggedInCustomer.saveToFile();
        System.out.printf(" New balance: RWF %.2f%n", loggedInCustomer.getBalance());
    }

    private static void logout() {
        System.out.println("\n Goodbye, " + loggedInCustomer.getName() + "!");
        loggedInCustomer = null;
        activeCart = null;
    }

    private static void saveAndExit() {
        System.out.println("\n Saving data...");
        Product.saveToFile();
        if (loggedInCustomer != null) {
            loggedInCustomer.saveToFile();
        }
        System.out.println(" Data saved. Goodbye! ");
        System.exit(0);
    }


    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print(" Please enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print(" Please enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print(" Please enter a valid number: ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}