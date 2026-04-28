import java.io.*;
import java.util.*;

class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private double balance;
    private Map<String, List<Double>> orderHistory = new HashMap<>();
    private Set<String> preferredCategories = new HashSet<>();

    public Customer(String name, double balance) {
        if (name == null || name.isEmpty()) {
            throw new CustomerException("Customer name cannot be empty");
        }
        if (balance < 0) {
            throw new CustomerException("Balance cannot be negative");
        }
        this.name = name;
        this.balance = balance;
    }

    public String getName() { return name; }
    public double getBalance() { return balance; }
    public Set<String> getPreferredCategories() { return preferredCategories; }
    public Map<String, List<Double>> getOrderHistory() { return orderHistory; }

    public int getOrderCount() {
        return orderHistory.size();
    }

    public void deductBalance(double amount) {
        if (amount > balance) {
            throw new CustomerException("Insufficient balance");
        }
        balance -= amount;
    }

    public void addBalance(double amount) {
        if (amount <= 0) throw new CustomerException("Amount must be positive");
        balance += amount;
        System.out.println("Added RWF " + amount + " to balance. New balance: RWF " + balance);
    }

    public void recordOrder(String orderId, List<Double> itemPrices) {
        orderHistory.put(orderId, new ArrayList<>(itemPrices));
        System.out.println("Order recorded for " + name + ": " + orderId);
        saveOrdersToFile();
        saveToFile();
    }

    public List<Double> getOrder(String orderId) {
        return orderHistory.getOrDefault(orderId, new ArrayList<>());
    }

    public void removeOrder(String orderId) {
        if (orderHistory.remove(orderId) != null) {
            System.out.println("Order removed: " + orderId);
            saveOrdersToFile();
            saveToFile();
        } else {
            System.out.println("Order not found: " + orderId);
        }
    }

    public void printOrderHistory() {
        System.out.println("\n--- Order History for " + name + " ---");
        if (orderHistory.isEmpty()) {
            System.out.println("  No orders yet.");
        } else {
            for (Map.Entry<String, List<Double>> entry : orderHistory.entrySet()) {
                System.out.println("  Order " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public void addPreferredCategory(String category) {
        boolean added = preferredCategories.add(category);
        if (added) {
            System.out.println("Category added for " + name + ": " + category);
            saveToFile();
        } else {
            System.out.println("Category already exists: " + category);
        }
    }

    public boolean prefersCategory(String category) {
        return preferredCategories.contains(category);
    }

    public void removePreferredCategory(String category) {
        if (preferredCategories.remove(category)) {
            System.out.println("Category removed: " + category);
            saveToFile();
        } else {
            System.out.println("Category not in preferences: " + category);
        }
    }

    public void printPreferredCategories() {
        System.out.println("Preferred categories for " + name + ": " + preferredCategories);
    }


    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String ORDERS_FILE = "orders.txt";

    public void saveToFile() {
        Map<String, Customer> allCustomers = loadAllCustomers();
        allCustomers.put(name, this);

        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            writer.println("=== CUSTOMER DATABASE ===");
            writer.println("Name|Balance|PreferredCategories");
            writer.println("----|-------|-------------------");

            for (Customer c : allCustomers.values()) {
                writer.printf("%s|%.2f|%s%n",
                        c.getName(),
                        c.getBalance(),
                        String.join(";", c.getPreferredCategories())
                );
            }
            writer.println("=== END OF DATABASE ===");
            System.out.println("✓ Customer saved to " + CUSTOMERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving customer: " + e.getMessage());
        }
    }

    public static Map<String, Customer> loadAllCustomers() {
        Map<String, Customer> customers = new HashMap<>();
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return customers;

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            boolean readingData = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Name|Balance|PreferredCategories")) {
                    readingData = true;
                    continue;
                }
                if (readingData && line.contains("===")) {
                    break;
                }
                if (readingData && !line.isEmpty() && line.contains("|") && !line.startsWith("-")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 2) {
                        String name = parts[0];
                        double balance = Double.parseDouble(parts[1]);
                        Customer customer = new Customer(name, balance);

                        if (parts.length >= 3 && !parts[2].isEmpty()) {
                            String[] categories = parts[2].split(";");
                            for (String cat : categories) {
                                if (!cat.isEmpty()) {
                                    customer.addPreferredCategory(cat);
                                }
                            }
                        }
                        customers.put(name, customer);
                    }
                }
            }
            System.out.println("✓ Customers loaded from " + CUSTOMERS_FILE);
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
        return customers;
    }

    public static Customer loadFromFile(String name) {
        return loadAllCustomers().get(name);
    }

    public void saveOrdersToFile() {
        Map<String, Map<String, List<Double>>> allOrders = loadAllOrders();
        allOrders.put(name, orderHistory);

        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            writer.println("=== ORDER HISTORY ===");
            writer.println("Customer|OrderID|Items|Total");
            writer.println("--------|-------|-----|-----");

            for (Map.Entry<String, Map<String, List<Double>>> entry : allOrders.entrySet()) {
                for (Map.Entry<String, List<Double>> order : entry.getValue().entrySet()) {
                    double total = order.getValue().stream().mapToDouble(Double::doubleValue).sum();
                    writer.printf("%s|%s|%d items|%.2f%n",
                            entry.getKey(), order.getKey(), order.getValue().size(), total);
                }
            }
            writer.println("=== END OF ORDERS ===");
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    public void loadOrderHistoryFromFile() {
        Map<String, Map<String, List<Double>>> allOrders = loadAllOrders();
        if (allOrders.containsKey(name)) {
            orderHistory.putAll(allOrders.get(name));
            System.out.println("Loaded " + orderHistory.size() + " orders for " + name);
        }
    }

    private static Map<String, Map<String, List<Double>>> loadAllOrders() {
        Map<String, Map<String, List<Double>>> allOrders = new HashMap<>();
        File file = new File(ORDERS_FILE);
        if (!file.exists()) return allOrders;

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            boolean readingData = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Customer|OrderID|Items|Total")) {
                    readingData = true;
                    continue;
                }
                if (readingData && line.contains("===")) {
                    break;
                }
                if (readingData && !line.isEmpty() && line.contains("|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 2) {
                        String customerName = parts[0];
                        String orderId = parts[1];
                        allOrders.computeIfAbsent(customerName, k -> new HashMap<>())
                                .put(orderId, new ArrayList<>());
                    }
                }
            }
        } catch (IOException e) {

        }
        return allOrders;
    }

    @Override
    public String toString() {
        return name + " | Balance: RWF " + balance + " | Orders: " + orderHistory.size();
    }
}