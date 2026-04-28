import java.io.*;
import java.util.*;

class DataFileManager {
    private static final String PRODUCTS_FILE = "products.dat";
    private static final String CUSTOMERS_FILE = "customers.dat";
    private static final String ORDERS_FILE = "orders.dat";
    private static final String CARTS_FILE = "carts.dat";

    // Save all products to file
    public static void saveProducts(Map<String, List<Product>> catalog) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCTS_FILE))) {
            oos.writeObject(catalog);
            System.out.println("✓ Products saved successfully to " + PRODUCTS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    // Load products from file
    @SuppressWarnings("unchecked")
    public static Map<String, List<Product>> loadProducts() {
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) {
            System.out.println("No existing products file found. Starting fresh.");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCTS_FILE))) {
            Map<String, List<Product>> catalog = (Map<String, List<Product>>) ois.readObject();
            System.out.println("✓ Products loaded successfully from " + PRODUCTS_FILE);
            return catalog;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading products: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Save customer data
    public static void saveCustomer(Customer customer) {
        Map<String, Customer> customers = loadAllCustomers();
        customers.put(customer.getName(), customer);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(customers);
            System.out.println("✓ Customer " + customer.getName() + " saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving customer: " + e.getMessage());
        }
    }

    // Load all customers
    @SuppressWarnings("unchecked")
    public static Map<String, Customer> loadAllCustomers() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CUSTOMERS_FILE))) {
            return (Map<String, Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading customers: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Save order history
    public static void saveOrders(String customerName, Map<String, List<Double>> orderHistory) {
        Map<String, Map<String, List<Double>>> allOrders = loadAllOrders();
        allOrders.put(customerName, orderHistory);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
            oos.writeObject(allOrders);
            System.out.println("✓ Orders saved for customer: " + customerName);
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    // Load all orders
    @SuppressWarnings("unchecked")
    public static Map<String, Map<String, List<Double>>> loadAllOrders() {
        File file = new File(ORDERS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDERS_FILE))) {
            return (Map<String, Map<String, List<Double>>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading orders: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Save cart data
    public static void saveCart(String customerName, List<CartItem> cartItems) {
        Map<String, List<CartItem>> allCarts = loadAllCarts();
        allCarts.put(customerName, cartItems);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CARTS_FILE))) {
            oos.writeObject(allCarts);
            System.out.println("✓ Cart saved for customer: " + customerName);
        } catch (IOException e) {
            System.err.println("Error saving cart: " + e.getMessage());
        }
    }

    // Load all carts
    @SuppressWarnings("unchecked")
    public static Map<String, List<CartItem>> loadAllCarts() {
        File file = new File(CARTS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CARTS_FILE))) {
            return (Map<String, List<CartItem>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading carts: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Delete all data files (for reset)
    public static void deleteAllData() {
        deleteFile(PRODUCTS_FILE);
        deleteFile(CUSTOMERS_FILE);
        deleteFile(ORDERS_FILE);
        deleteFile(CARTS_FILE);
        System.out.println("✓ All data files cleared");
    }

    private static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}