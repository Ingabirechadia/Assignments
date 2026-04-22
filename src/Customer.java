import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


class Customer {
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

    public void deductBalance(double amount) {
        if (amount > balance) {
            throw new CustomerException("Insufficient balance");
        }
        balance -= amount;
    }


    public void recordOrder(String orderId, List<Double> itemPrices) {
        orderHistory.put(orderId, new ArrayList<>(itemPrices));
        System.out.println("Order recorded for " + name + ": " + orderId);
    }


    public List<Double> getOrder(String orderId) {
        return orderHistory.getOrDefault(orderId, List.of());
    }


    public void removeOrder(String orderId) {
        if (orderHistory.remove(orderId) != null) {
            System.out.println("Order removed: " + orderId);
        } else {
            System.out.println("Order not found: " + orderId);
        }
    }


    public void printOrderHistory() {
        System.out.println("\n--- Order History for " + name + " ---");
        if (orderHistory.isEmpty()) {
            System.out.println("  No orders yet.");
        }
        for (Map.Entry<String, List<Double>> entry : orderHistory.entrySet()) {
            System.out.println("  Order " + entry.getKey() + ": " + entry.getValue());
        }
    }


    public void addPreferredCategory(String category) {
        boolean added = preferredCategories.add(category);
        if (added) {
            System.out.println("Category added for " + name + ": " + category);
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
        } else {
            System.out.println("Category not in preferences: " + category);
        }
    }


    public void printPreferredCategories() {
        System.out.println("Preferred categories for " + name + ": " + preferredCategories);
    }
}
